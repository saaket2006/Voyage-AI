package com.voyageai.auth;

import com.voyageai.auth.dto.*;
import com.voyageai.common.exception.ConflictException;
import com.voyageai.common.exception.ResourceNotFoundException;
import com.voyageai.common.exception.UnauthorizedException;
import com.voyageai.security.JwtTokenProvider;
import com.voyageai.user.User;
import com.voyageai.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("An account with email '" + normalizedEmail + "' already exists.");
        }

        String userId = UUID.randomUUID().toString();
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = new User(userId, normalizedEmail, passwordHash, request.getDisplayName().trim());
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getDisplayName());
        long expiresInSeconds = jwtTokenProvider.getExpirationMs() / 1000;

        return new AuthResponse(
                token,
                "Bearer",
                expiresInSeconds,
                new UserSummaryDto(user.getId(), user.getEmail(), user.getDisplayName())
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getDisplayName());
        long expiresInSeconds = jwtTokenProvider.getExpirationMs() / 1000;

        return new AuthResponse(
                token,
                "Bearer",
                expiresInSeconds,
                new UserSummaryDto(user.getId(), user.getEmail(), user.getDisplayName())
        );
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getPreferences(),
                user.getCreatedAt()
        );
    }

    @Transactional
    public UserProfileResponse updatePreferences(String userId, UpdatePreferencesRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setPreferences(request.getPreferences());
        userRepository.save(user);

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getPreferences(),
                user.getCreatedAt()
        );
    }
}
