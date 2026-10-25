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
                    // Also populate system property if not already set by system env or JVM flag
                    if (System.getProperty(key) == null && System.getenv(key) == null) {
                        System.setProperty(key, cleanVal);
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
     * Locates .env starting from working directory up to parent levels.
     */
    public static Path findEnvFile() {
        Path[] candidates = new Path[]{
                Path.of(".env"),
                Path.of("../.env"),
                Path.of("../../.env")
        };

        for (Path p : candidates) {
            if (Files.isRegularFile(p)) {
                return p;
            }
        }
        return null;
    }
}
