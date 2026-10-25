-- ============================================================================
-- Voyage-AI Database Migration V3: User Profile Enhancements & Destination Indexes
-- Adds external auth provider tracking, profile image URL, and provider indexes
-- ============================================================================

-- 1. Extend Users table for profile persistence and federated authentication
ALTER TABLE users ADD COLUMN auth_provider VARCHAR(50) NOT NULL DEFAULT 'LOCAL';
ALTER TABLE users ADD COLUMN provider_subject VARCHAR(255) NULL;
ALTER TABLE users ADD COLUMN profile_image_url VARCHAR(512) NULL;

CREATE INDEX idx_users_provider ON users(auth_provider, provider_subject);

-- 2. Add composite index on destinations for external source records
CREATE INDEX idx_destinations_provider ON destinations(source_provider, provider_record_id);
