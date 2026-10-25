package com.voyageai.auth;

import com.voyageai.auth.dto.*;
import com.voyageai.common.exception.ConflictException;
import com.voyageai.common.exception.ResourceNotFoundException;
import com.voyageai.common.exception.UnauthorizedException;
import com.voyageai.security.JwtTokenProvider;
import com.voyageai.user.User;
import com.voyageai.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final String adminEmails;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       ObjectMapper objectMapper,
                       @Value("${app.security.admin-emails:admin@voyageai.local,admin@voyage.ai}") String adminEmails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        this.adminEmails = adminEmails;
    }

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       @Value("${app.security.admin-emails:admin@voyageai.local,admin@voyage.ai}") String adminEmails) {
        this(userRepository, passwordEncoder, jwtTokenProvider, new ObjectMapper(), adminEmails);
    }

    private boolean isAdminEmail(String email) {
        if (email == null || adminEmails == null) return false;
        String clean = email.trim().toLowerCase(java.util.Locale.ENGLISH);
        return java.util.Arrays.stream(adminEmails.split(","))
                .map(s -> s.trim().toLowerCase(java.util.Locale.ENGLISH))
                .filter(s -> !s.isEmpty())
                .anyMatch(pattern -> {
                    if (pattern.startsWith("*@")) {
                        return clean.endsWith(pattern.substring(1));
                    } else if (pattern.contains("*")) {
                        String regex = pattern.replace(".", "\\.").replace("*", ".*");
                        return clean.matches(regex);
                    }
                    return clean.equals(pattern);
                });
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
        user.setRole(isAdminEmail(normalizedEmail) ? "ROLE_ADMIN" : "ROLE_USER");
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole());
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

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole());
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

        return toProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse syncProfile(String principalUserId, com.voyageai.user.dto.UserSyncRequest request) {
        User user = userRepository.findById(principalUserId)
                .orElseGet(() -> {
                    if (request.getEmail() != null && !request.getEmail().isBlank()) {
                        return userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                                .orElseGet(() -> {
                                    String newId = principalUserId != null && !principalUserId.isBlank() ? principalUserId : UUID.randomUUID().toString();
                                    String email = request.getEmail().trim().toLowerCase();
                                    String displayName = request.getDisplayName() != null ? request.getDisplayName().trim() : "Traveler";
                                    return new User(newId, email, "", displayName, request.getAuthProvider(), request.getProviderSubject());
                                });
                    }
                    throw new ResourceNotFoundException("User not found with id: " + principalUserId);
                });

        if (request.getDisplayName() != null && !request.getDisplayName().isBlank()) {
            user.setDisplayName(request.getDisplayName().trim());
        }
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl().trim());
        }
        if (request.getAuthProvider() != null && !request.getAuthProvider().isBlank()) {
            user.setAuthProvider(request.getAuthProvider().trim());
        }
        if (request.getProviderSubject() != null && !request.getProviderSubject().isBlank()) {
            user.setProviderSubject(request.getProviderSubject().trim());
        }
        user = userRepository.save(user);

        return toProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(String userId, com.voyageai.user.dto.UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getDisplayName() != null && !request.getDisplayName().isBlank()) {
            user.setDisplayName(request.getDisplayName().trim());
        }
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl().trim());
        }
        if (request.getPreferences() != null) {
            user.setPreferences(request.getPreferences());
        }
        user = userRepository.save(user);

        return toProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updatePreferences(String userId, UpdatePreferencesRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setPreferences(request.getPreferences());
        user = userRepository.save(user);

        return toProfileResponse(user);
    }

    @Transactional(readOnly = true)
    public List<String> getWishlist(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return extractWishlistFromPreferences(user.getPreferences());
    }

    @Transactional
    public List<String> addToWishlist(String userId, String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            return getWishlist(userId);
        }
        String cleanId = destinationId.trim().toLowerCase();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> list = new ArrayList<>(extractWishlistFromPreferences(user.getPreferences()));
        if (!list.contains(cleanId)) {
            list.add(cleanId);
            user.setPreferences(saveWishlistToPreferences(user.getPreferences(), list));
            userRepository.save(user);
        }
        return list;
    }

    @Transactional
    public List<String> removeFromWishlist(String userId, String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            return getWishlist(userId);
        }
        String cleanId = destinationId.trim().toLowerCase();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> list = new ArrayList<>(extractWishlistFromPreferences(user.getPreferences()));
        if (list.remove(cleanId)) {
            user.setPreferences(saveWishlistToPreferences(user.getPreferences(), list));
            userRepository.save(user);
        }
        return list;
    }

    private List<String> extractWishlistFromPreferences(String preferencesJson) {
        if (preferencesJson == null || preferencesJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            JsonNode root = objectMapper.readTree(preferencesJson);
            if (root.has("savedDestinationIds") && root.get("savedDestinationIds").isArray()) {
                List<String> result = new ArrayList<>();
                for (JsonNode node : root.get("savedDestinationIds")) {
                    if (node.isTextual() && !node.asText().isBlank() && !result.contains(node.asText().toLowerCase())) {
                        result.add(node.asText().toLowerCase());
                    }
                }
                return result;
            }
        } catch (Exception e) {
            log.warn("Failed to parse user preferences JSON for wishlist: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    private String saveWishlistToPreferences(String preferencesJson, List<String> wishlist) {
        try {
            ObjectNode root;
            if (preferencesJson != null && !preferencesJson.isBlank()) {
                JsonNode existing = objectMapper.readTree(preferencesJson);
                if (existing.isObject()) {
                    root = (ObjectNode) existing;
                } else {
                    root = objectMapper.createObjectNode();
                }
            } else {
                root = objectMapper.createObjectNode();
            }
            ArrayNode arr = root.putArray("savedDestinationIds");
            for (String id : wishlist) {
                arr.add(id);
            }
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            log.warn("Failed to serialize wishlist to user preferences: {}", e.getMessage());
            return preferencesJson;
        }
    }

    private UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getProfileImageUrl(),
                user.getAuthProvider(),
                user.getPreferences(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
