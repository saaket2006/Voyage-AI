package com.voyageai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility for discovering and parsing a local .env file.
 * Safely strips surrounding quotes and whitespace from values.
 * Allows platform/system environment variables to maintain precedence.
 */
public final class DotenvLoader {

    private static final Logger log = LoggerFactory.getLogger(DotenvLoader.class);

    private DotenvLoader() {
    }

    /**
     * Discovers a .env file searching current and parent directories,
     * parses its key-value pairs, and returns them as a Map.
     */
    public static Map<String, Object> load() {
        Map<String, Object> properties = new HashMap<>();
        Path envPath = findEnvFile();

        if (envPath == null) {
            log.debug("No local .env file discovered; relying on system environment variables.");
            return properties;
        }

        log.info("Loading environment configuration from: {}", envPath.toAbsolutePath().normalize());

        try (BufferedReader reader = Files.newBufferedReader(envPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int eqIdx = line.indexOf('=');
                if (eqIdx <= 0) {
                    continue;
                }

                String key = line.substring(0, eqIdx).trim();
                String rawVal = line.substring(eqIdx + 1).trim();

                // Strip matching surrounding quotes if present
                String cleanVal = cleanValue(rawVal);

                    if (!key.isEmpty()) {
                    properties.put(key, cleanVal);
                    // Also populate system property if not already set by non-blank system env or JVM flag
                    String currentSysProp = System.getProperty(key);
                    String currentEnvVal = System.getenv(key);
                    if ((currentSysProp == null || currentSysProp.isBlank()) &&
                        (currentEnvVal == null || currentEnvVal.isBlank())) {
                        System.setProperty(key, cleanVal);
                    }
                    if ("GEMINI_API_KEY".equalsIgnoreCase(key)) {
                        System.setProperty("gemini.api-key", cleanVal);
                    }
                }
            }
        } catch (IOException ex) {
            log.warn("Failed to read .env file at {}: {}", envPath, ex.getMessage());
        }

        return properties;
    }

    /**
     * Strips matching surrounding single or double quotes, and trailing inline comments.
     */
    public static String cleanValue(String val) {
        if (val == null) {
            return "";
        }
        String clean = val.trim();
        if ((clean.startsWith("\"") && clean.endsWith("\"")) ||
            (clean.startsWith("'") && clean.endsWith("'"))) {
            if (clean.length() >= 2) {
                clean = clean.substring(1, clean.length() - 1);
            }
        }
        return clean.trim();
    }

    /**
     * Locates .env starting from working directory, user.dir, up to parent levels.
     */
    public static Path findEnvFile() {
        java.util.List<Path> candidates = new java.util.ArrayList<>();
        candidates.add(Path.of(".env"));
        candidates.add(Path.of("../.env"));
        candidates.add(Path.of("../../.env"));
        candidates.add(Path.of("/app/.env"));

        String userDir = System.getProperty("user.dir");
        if (userDir != null && !userDir.isBlank()) {
            candidates.add(Path.of(userDir, ".env"));
            candidates.add(Path.of(userDir, "..", ".env"));
        }

        for (Path p : candidates) {
            try {
                if (Files.isRegularFile(p)) {
                    return p.toAbsolutePath().normalize();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}

