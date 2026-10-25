package com.voyageai.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Standard JWT Token Provider implementing HMAC-SHA256 token issuance and validation.
 * Complies with RFC 7519 and docs/JWT_AUTH_DESIGN.md.
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] secretBytes;
    private final long expirationMs;
    private final ObjectMapper objectMapper;

    public JwtTokenProvider(
            @Value("${jwt.secret:voyageai_dev_secret_key_placeholder_not_for_production_min_256_bits_required!}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
        this.objectMapper = new ObjectMapper();
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public String generateToken(String userId, String email, String displayName) {
        return generateToken(userId, email, displayName, "ROLE_USER");
    }

    public String generateToken(String userId, String email, String displayName, String role) {
        long nowSeconds = Instant.now().getEpochSecond();
        long expSeconds = nowSeconds + (expirationMs / 1000);

        try {
            // Header
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            String encodedHeader = base64UrlEncode(objectMapper.writeValueAsBytes(header));

            // Payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", userId);
            payload.put("email", email);
            payload.put("displayName", displayName);
            payload.put("role", role != null ? role : "ROLE_USER");
            payload.put("iat", nowSeconds);
            payload.put("exp", expSeconds);
            String encodedPayload = base64UrlEncode(objectMapper.writeValueAsBytes(payload));

            // Signature
            String dataToSign = encodedHeader + "." + encodedPayload;
            byte[] signature = signHmacSha256(dataToSign.getBytes(StandardCharsets.UTF_8));
            String encodedSignature = base64UrlEncode(signature);

            return dataToSign + "." + encodedSignature;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        return parseClaims(token).isPresent();
    }

    public Optional<UserPrincipal> getUserPrincipalFromToken(String token) {
        return parseClaims(token).map(claims -> {
            String userId = (String) claims.get("sub");
            String email = (String) claims.get("email");
            String displayName = (String) claims.get("displayName");
            String role = (String) claims.getOrDefault("role", "ROLE_USER");
            return UserPrincipal.create(userId, email, displayName, role);
        });
    }

    private Optional<Map<String, Object>> parseClaims(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            log.debug("JWT token does not have 3 parts");
            return Optional.empty();
        }

        try {
            String encodedHeader = parts[0];
            String encodedPayload = parts[1];
            String encodedSignature = parts[2];

            // 1. Verify signature
            String dataToSign = encodedHeader + "." + encodedPayload;
            byte[] expectedSignature = signHmacSha256(dataToSign.getBytes(StandardCharsets.UTF_8));
            byte[] actualSignature = base64UrlDecode(encodedSignature);

            if (!MessageDigest.isEqual(expectedSignature, actualSignature)) {
                log.debug("JWT token signature mismatch");
                return Optional.empty();
            }

            // 2. Parse payload
            byte[] payloadBytes = base64UrlDecode(encodedPayload);
            Map<String, Object> claims = objectMapper.readValue(payloadBytes, new TypeReference<Map<String, Object>>() {});

            // 3. Verify expiration
            Number expNumber = (Number) claims.get("exp");
            if (expNumber == null) {
                log.debug("JWT token missing exp claim");
                return Optional.empty();
            }

            long expSeconds = expNumber.longValue();
            long nowSeconds = Instant.now().getEpochSecond();
            if (nowSeconds >= expSeconds) {
                log.debug("JWT token is expired (exp: {}, now: {})", expSeconds, nowSeconds);
                return Optional.empty();
            }

            return Optional.of(claims);
        } catch (Exception e) {
            log.debug("Failed to parse JWT token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private byte[] signHmacSha256(byte[] data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretBytes, HMAC_ALGORITHM);
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private byte[] base64UrlDecode(String str) {
        return Base64.getUrlDecoder().decode(str);
    }
}
