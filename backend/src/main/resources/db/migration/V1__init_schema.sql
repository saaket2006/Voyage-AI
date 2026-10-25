-- ============================================================================
-- Voyage-AI Database Migration V1: Core Relational Schema
-- Supports User Identity, Authentic Destinations, Travel Listings, Trips & Itineraries
-- Fully compatible with MySQL 8.0 and H2 (MySQL Mode)
-- ============================================================================

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    preferences TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- 2. Destinations Table (with Authentic Provenance Columns)
CREATE TABLE IF NOT EXISTS destinations (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    state VARCHAR(64) NOT NULL,
    country VARCHAR(64) NOT NULL DEFAULT 'India',
    region VARCHAR(64) NOT NULL,
    tagline VARCHAR(255) NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(512) NULL,
    hero_image_url VARCHAR(512) NULL,
    travel_styles VARCHAR(255) NULL,
    budget_tier VARCHAR(32) NOT NULL DEFAULT 'Comfort',
    recommended_duration_days INT NOT NULL DEFAULT 3,
    best_time_to_visit VARCHAR(128) NULL,
    latitude DOUBLE NULL,
    longitude DOUBLE NULL,
    source_provider VARCHAR(64) NOT NULL DEFAULT 'OPEN_STREET_MAP',
    provider_record_id VARCHAR(128) NOT NULL,
    retrieved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stale_after TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    license_type VARCHAR(64) NOT NULL DEFAULT 'ODbL',
    attribution_text VARCHAR(255) NOT NULL,
    attribution_url VARCHAR(512) NULL,
    verification_status VARCHAR(32) NOT NULL DEFAULT 'UNVERIFIED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_destinations_region ON destinations(region);
CREATE INDEX idx_destinations_budget_tier ON destinations(budget_tier);
CREATE INDEX idx_destinations_name ON destinations(name);

-- 3. Travel Listings Table (Accommodations, Dining, Attractions, Activities)
CREATE TABLE IF NOT EXISTS travel_listings (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    destination_id VARCHAR(64) NOT NULL,
    listing_type VARCHAR(32) NOT NULL,
    name VARCHAR(128) NOT NULL,
    category VARCHAR(64) NULL,
    description TEXT NULL,
    image_url VARCHAR(512) NULL,
    price_estimate DECIMAL(10,2) NULL,
    price_range VARCHAR(16) NULL,
    currency VARCHAR(8) NOT NULL DEFAULT 'INR',
    rating DECIMAL(3,2) NULL,
    must_try VARCHAR(128) NULL,
    estimated_hours DECIMAL(4,2) NULL,
    entry_fee DECIMAL(10,2) NULL,
    highlights TEXT NULL,
    address VARCHAR(255) NULL,
    latitude DOUBLE NULL,
    longitude DOUBLE NULL,
    source_provider VARCHAR(64) NOT NULL DEFAULT 'OPEN_STREET_MAP',
    provider_record_id VARCHAR(128) NOT NULL,
    retrieved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stale_after TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    license_type VARCHAR(64) NOT NULL DEFAULT 'ODbL',
    attribution_text VARCHAR(255) NOT NULL,
    attribution_url VARCHAR(512) NULL,
    verification_status VARCHAR(32) NOT NULL DEFAULT 'UNVERIFIED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_listings_destination FOREIGN KEY (destination_id) REFERENCES destinations(id) ON DELETE CASCADE
);

CREATE INDEX idx_listings_destination ON travel_listings(destination_id);
CREATE INDEX idx_listings_type ON travel_listings(listing_type);

-- 4. Trips Table (Private, User-Owned)
CREATE TABLE IF NOT EXISTS trips (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    destination_id VARCHAR(64) NULL,
    destination_name VARCHAR(128) NULL,
    title VARCHAR(128) NOT NULL,
    description TEXT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    traveler_count INT NOT NULL DEFAULT 1,
    target_budget DECIMAL(12,2) NULL,
    currency VARCHAR(8) NOT NULL DEFAULT 'INR',
    status VARCHAR(32) NOT NULL DEFAULT 'PLANNING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trips_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_trips_user ON trips(user_id);

-- 5. Itinerary Days Table
CREATE TABLE IF NOT EXISTS itinerary_days (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    trip_id VARCHAR(36) NOT NULL,
    day_number INT NOT NULL,
    date DATE NULL,
    title VARCHAR(128) NULL,
    notes TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_days_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT uk_trip_day UNIQUE (trip_id, day_number)
);

CREATE INDEX idx_days_trip ON itinerary_days(trip_id);

-- 6. Itinerary Items Table
CREATE TABLE IF NOT EXISTS itinerary_items (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    itinerary_day_id VARCHAR(36) NOT NULL,
    listing_id VARCHAR(64) NULL,
    title VARCHAR(128) NOT NULL,
    category VARCHAR(32) NOT NULL DEFAULT 'CUSTOM',
    notes TEXT NULL,
    suggested_time VARCHAR(16) NULL,
    duration_minutes INT NULL,
    estimated_cost DECIMAL(10,2) NULL,
    currency VARCHAR(8) NOT NULL DEFAULT 'INR',
    item_order INT NOT NULL DEFAULT 0,
    origin_type VARCHAR(32) NOT NULL DEFAULT 'MANUAL',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_items_day FOREIGN KEY (itinerary_day_id) REFERENCES itinerary_days(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_listing FOREIGN KEY (listing_id) REFERENCES travel_listings(id) ON DELETE SET NULL
);

CREATE INDEX idx_items_day ON itinerary_items(itinerary_day_id);
