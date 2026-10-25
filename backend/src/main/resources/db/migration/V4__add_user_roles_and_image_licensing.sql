-- ============================================================================
-- Voyage-AI Database Migration V4: User Roles and Destination Image Licensing
-- Adds role-based access control to users and granular media licensing/provenance
-- tracking to destinations to distinguish text and photography licensing.
-- ============================================================================

-- 1. Add server-enforced role to users (default: ROLE_USER)
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER';

-- 2. Add granular image attribution and licensing columns to destinations
ALTER TABLE destinations ADD COLUMN image_attribution_text VARCHAR(255) NULL;
ALTER TABLE destinations ADD COLUMN image_license_type VARCHAR(64) NULL;
ALTER TABLE destinations ADD COLUMN image_license_url VARCHAR(512) NULL;
ALTER TABLE destinations ADD COLUMN image_source_url VARCHAR(512) NULL;
ALTER TABLE destinations ADD COLUMN curation_status VARCHAR(32) NOT NULL DEFAULT 'VERIFIED';

-- 3. Update existing seed destination records to record accurate image licensing
UPDATE destinations 
SET image_attribution_text = attribution_text,
    image_license_type = 'Unsplash License (Editorial)',
    image_license_url = 'https://unsplash.com/license',
    image_source_url = image_url,
    curation_status = 'VERIFIED'
WHERE image_attribution_text IS NULL;
