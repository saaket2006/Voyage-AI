-- ============================================================================
-- Voyage-AI Database Migration V7: Seed Default Traveler Account
-- Seeds canonical default traveler account (traveler@voyageai.local) with initialized
-- empty saved destinations wishlist preferences to guarantee out-of-the-box persistence.
-- Password hash corresponds to 'Password123!'.
-- ============================================================================

INSERT INTO users (
    id, email, password_hash, display_name, preferences, auth_provider, role, created_at, updated_at
)
SELECT 
    'usr-traveler-default-001',
    'traveler@voyageai.local',
    '$2a$10$8I6Dl297MKhhvqkHrK428O/AOxKUFizrrA4f1giwTDYHPu.NTSWM6',
    'Voyage Traveler',
    '{"savedDestinationIds":[]}',
    'LOCAL',
    'ROLE_USER',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'traveler@voyageai.local');
