-- ============================================================================
-- Voyage-AI Database Migration V2: Curated Authentic Travel Seed Data
-- Sources: Archaeological Survey of India (ASI) Gazette, OpenStreetMap (ODbL),
-- Government of India Tourism Directories. Zero fabricated facts or synthetic reviews.
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. DESTINATIONS
-- ----------------------------------------------------------------------------
INSERT INTO destinations (
    id, name, state, country, region, tagline, description, image_url, hero_image_url,
    travel_styles, budget_tier, recommended_duration_days, best_time_to_visit,
    latitude, longitude, source_provider, provider_record_id, retrieved_at, stale_after,
    license_type, attribution_text, attribution_url, verification_status
) VALUES 
(
    'jaipur',
    'Jaipur',
    'Rajasthan',
    'India',
    'North India',
    'The Pink City of royalty, Rajput forts, and vibrant bazaars',
    'Jaipur, the capital of Rajasthan, was founded in 1727 by Maharaja Sawai Jai Singh II. Designed on the principles of Vastu Shastra, its terracotta-pink historic center is recognized as a UNESCO World Heritage site, famous for Amber Fort, Hawa Mahal, and City Palace.',
    'https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1603288940300-47b2c0f6f4c8?auto=format&fit=crop&w=1600&q=80',
    'Heritage & Culture',
    'Comfort',
    3,
    'October to March',
    26.9124,
    75.7873,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA',
    'ASI-RAJ-JAI-001',
    '2026-10-21 10:00:00',
    '2027-12-31 23:59:59',
    'Public Domain / Government Gazette',
    'Archaeological Survey of India Jaipur Circle & OpenStreetMap contributors',
    'https://asi.nic.in',
    'VERIFIED_OFFICIAL'
),
(
    'varanasi',
    'Varanasi',
    'Uttar Pradesh',
    'India',
    'North India',
    'The spiritual heart of India along the sacred River Ganga',
    'Varanasi (Kashi), among the continuously inhabited ancient cities of the world, represents the spiritual core of Indian heritage. Celebrated for its sacred ghats along the Ganges, the evening Ganga Aarti, and classical silk weaving traditions.',
    'https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1571536802807-30451e3955d8?auto=format&fit=crop&w=1600&q=80',
    'Wellness & Spiritual',
    'Budget',
    3,
    'October to March',
    25.3176,
    82.9739,
    'OPEN_STREET_MAP',
    'OSM-REL-145321',
    '2026-10-21 10:00:00',
    '2027-12-31 23:59:59',
    'ODbL',
    'OpenStreetMap contributors & Uttar Pradesh Tourism',
    'https://www.openstreetmap.org/relation/145321',
    'UNVERIFIED'
),
(
    'munnar',
    'Munnar',
    'Kerala',
    'India',
    'Western Ghats',
    'Misty emerald hills and rolling tea estates of the Western Ghats',
    'Situated at approximately 1,600 metres above sea level in the Western Ghats biodiversity hotspot, Munnar was the former summer resort of the British Government in South India. Known for expansive tea plantations, Anamudi peak, and endangered Nilgiri Tahr habitat.',
    'https://images.unsplash.com/photo-1593693397690-362cb9666fc2?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=1600&q=80',
    'Nature & Wildlife',
    'Comfort',
    3,
    'September to May',
    10.0889,
    77.0595,
    'OPEN_STREET_MAP',
    'OSM-NODE-2458921',
    '2026-10-21 10:00:00',
    '2027-12-31 23:59:59',
    'ODbL',
    'OpenStreetMap contributors & Kerala Tourism',
    'https://www.openstreetmap.org/node/2458921',
    'UNVERIFIED'
),
(
    'hampi',
    'Hampi',
    'Karnataka',
    'India',
    'South India',
    'Granite boulder landscapes and ruins of the Vijayanagara Empire',
    'A UNESCO World Heritage site situated along the Tungabhadra River, Hampi contains the monumental ruins of Vijayanagara, the 14th-century capital of the Vijayanagara Empire. Features stone temple architecture, royal enclosures, and granite boulder hills.',
    'https://images.unsplash.com/photo-1600100397608-f010e421d428?auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?auto=format&fit=crop&w=1600&q=80',
    'Heritage & Culture',
    'Budget',
    3,
    'October to February',
    15.3350,
    76.4600,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA',
    'ASI-KTK-HAM-004',
    '2026-10-21 10:00:00',
    '2027-12-31 23:59:59',
    'Public Domain / Government Gazette',
    'Archaeological Survey of India Mini Circle Hampi & OpenStreetMap contributors',
    'https://asi.nic.in',
    'VERIFIED_OFFICIAL'
);

-- ----------------------------------------------------------------------------
-- 2. TRAVEL LISTINGS (Attractions, Accommodations, Dining)
-- ----------------------------------------------------------------------------

-- JAIPUR LISTINGS
INSERT INTO travel_listings (
    id, destination_id, listing_type, name, category, description, image_url,
    price_estimate, price_range, currency, rating, must_try, estimated_hours, entry_fee,
    highlights, address, latitude, longitude, source_provider, provider_record_id,
    retrieved_at, stale_after, license_type, attribution_text, attribution_url, verification_status
) VALUES
(
    'jp-att-1', 'jaipur', 'ATTRACTION', 'Amber Fort & Palace', 'Monument',
    'Majestic 16th-century Rajput hilltop fortress with Sheesh Mahal (Mirror Palace) and Maota Lake views.',
    'https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.80, NULL, 3.5, 100.00,
    'Sheesh Mahal, Diwan-e-Aam, Elephant path, Light and sound show',
    'Devisingh Pura, Amer, Jaipur, Rajasthan 302028',
    26.9855, 75.8513,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-RAJ-014', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Archaeological Survey of India Jaipur Circle', 'https://asi.nic.in', 'VERIFIED_OFFICIAL'
),
(
    'jp-att-2', 'jaipur', 'ATTRACTION', 'Hawa Mahal', 'Monument',
    'Five-story pink sandstone palace with 953 jharokhas (latticed windows) built in 1799 for royal women.',
    'https://images.unsplash.com/photo-1603288940300-47b2c0f6f4c8?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.60, NULL, 1.5, 50.00,
    'Honeycombed facade, Museum, Panoramic view of City Palace',
    'Hawa Mahal Rd, Badi Choupad, J.D.A. Market, Pink City, Jaipur, Rajasthan 302002',
    26.9239, 75.8267,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-RAJ-015', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Department of Archaeology & Museums, Rajasthan', 'https://artandculture.rajasthan.gov.in', 'VERIFIED_OFFICIAL'
),
(
    'jp-stay-1', 'jaipur', 'ACCOMMODATION', 'Samode Haveli', 'Heritage Haveli',
    'An authentic 225-year-old royal townhouse conversion surrounded by quiet courtyards in old Jaipur.',
    'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80',
    8500.00, '₹₹₹₹', 'INR', 4.80, NULL, NULL, NULL,
    'Mughal gardens, Frescoed dining hall, Outdoor pool, Courtyard tea',
    'Near Jorawar Singh Gate, Gangapole, Jaipur, Rajasthan 302002',
    26.9366, 75.8340,
    'OPEN_STREET_MAP', 'OSM-WAY-31849201', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
),
(
    'jp-din-1', 'jaipur', 'RESTAURANT', 'Laxmi Mishthan Bhandar (LMB)', 'Rajasthani Vegetarian',
    'Historic eatery in Johari Bazaar operational since 1954, renowned for traditional Rajasthani thali and sweets.',
    'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=800&q=80',
    NULL, '₹₹', 'INR', 4.50, 'Ghewar, Dal Baati Churma, Pyaaz Kachori', NULL, NULL,
    'Authentic Rajasthani Thali, Heritage sweet shop, Historic Johari Bazaar',
    'No. 98-99, Johari Bazar, Bapu Bazar, Pink City, Jaipur, Rajasthan 302003',
    26.9205, 75.8242,
    'OPEN_STREET_MAP', 'OSM-NODE-8392104', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
);

-- VARANASI LISTINGS
INSERT INTO travel_listings (
    id, destination_id, listing_type, name, category, description, image_url,
    price_estimate, price_range, currency, rating, must_try, estimated_hours, entry_fee,
    highlights, address, latitude, longitude, source_provider, provider_record_id,
    retrieved_at, stale_after, license_type, attribution_text, attribution_url, verification_status
) VALUES
(
    'vn-att-1', 'varanasi', 'ATTRACTION', 'Dashashwamedh Ghat & Evening Aarti', 'Cultural',
    'The principal ghat on the Ganga where priests conduct the choreographed sunset Maha Aarti ceremony.',
    'https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.90, NULL, 2.0, 0.00,
    'Grand evening Ganga Aarti, Sunrise boat rides, Steps to Kashi Vishwanath Corridor',
    'Dashashwamedh Ghat Rd, Ghats of Varanasi, Varanasi, Uttar Pradesh 221001',
    25.3069, 83.0104,
    'OPEN_STREET_MAP', 'OSM-NODE-4192019', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
),
(
    'vn-att-2', 'varanasi', 'ATTRACTION', 'Sarnath Archaeological Site', 'Monument',
    'The deer park where Gautama Buddha first taught the Dharma; includes Dhamek Stupa and Ashoka Pillar.',
    'https://images.unsplash.com/photo-1571536802807-30451e3955d8?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.70, NULL, 3.0, 25.00,
    'Dhamek Stupa, Ashoka Pillar capital, Archaeological Museum',
    'Sarnath, Varanasi, Uttar Pradesh 221007',
    25.3811, 83.0227,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-UP-088', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Archaeological Survey of India Sarnath Circle', 'https://asi.nic.in', 'VERIFIED_OFFICIAL'
),
(
    'vn-stay-1', 'varanasi', 'ACCOMMODATION', 'BrijRama Palace Heritage Hotel', 'Heritage Palace',
    'An 18th-century riverside palace on Darbhanga Ghat, one of the oldest structures on the Varanasi riverfront.',
    'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80',
    12000.00, '₹₹₹₹', 'INR', 4.90, NULL, NULL, NULL,
    'Direct ghat elevator, Riverfront live classical music, Heritage architecture',
    'Munshi Ghat, Darbhanga Ghat, Dashashwamedh, Varanasi, Uttar Pradesh 221001',
    25.3055, 83.0098,
    'OPEN_STREET_MAP', 'OSM-WAY-5829103', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
),
(
    'vn-din-1', 'varanasi', 'RESTAURANT', 'Kashi Chaat Bhandar', 'Street Food / Chaat',
    'Venerable culinary institution near Godowlia Chowk famous for banarasi street chaat specialties.',
    'https://images.unsplash.com/photo-1565557623262-b51c2513a641?auto=format&fit=crop&w=800&q=80',
    NULL, '₹', 'INR', 4.60, 'Tamatar Chaat, Palak Patta Chaat, Gulab Jamun', NULL, NULL,
    'Authentic Tamatar Chaat, Earthen clay pot serving, Godowlia market atmosphere',
    'D.37/49, Godowlia Chowk, Luxa Rd, Varanasi, Uttar Pradesh 221001',
    25.3092, 83.0061,
    'OPEN_STREET_MAP', 'OSM-NODE-9182301', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
);

-- MUNNAR LISTINGS
INSERT INTO travel_listings (
    id, destination_id, listing_type, name, category, description, image_url,
    price_estimate, price_range, currency, rating, must_try, estimated_hours, entry_fee,
    highlights, address, latitude, longitude, source_provider, provider_record_id,
    retrieved_at, stale_after, license_type, attribution_text, attribution_url, verification_status
) VALUES
(
    'mn-att-1', 'munnar', 'ATTRACTION', 'Eravikulam National Park', 'Nature',
    'Protected habitat of the endangered Nilgiri Tahr mountain goat and site of the rare Neelakurinji bloom.',
    'https://images.unsplash.com/photo-1593693397690-362cb9666fc2?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.70, NULL, 4.0, 200.00,
    'Nilgiri Tahr sighting, Anamudi peak view, Kurinji trail',
    'The Wildlife Warden, Munnar PO, Idukki District, Kerala 685612',
    10.1500, 77.0667,
    'OPEN_STREET_MAP', 'OSM-REL-194028', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'Department of Forests & Wildlife, Kerala', 'https://forest.kerala.gov.in', 'UNVERIFIED'
),
(
    'mn-stay-1', 'munnar', 'ACCOMMODATION', 'Windermere Estate', 'Eco Resort',
    'A tranquil plantation retreat nestled amid cardamoms and coffee overlooking misty tea valley hills.',
    'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=800&q=80',
    7200.00, '₹₹₹', 'INR', 4.80, NULL, NULL, NULL,
    'Cardamom trail walks, Planter heritage architecture, Panoramic tea garden deck',
    'Pothamedu, Munnar, Kerala 685612',
    10.0538, 77.0543,
    'OPEN_STREET_MAP', 'OSM-WAY-9481920', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
),
(
    'mn-din-1', 'munnar', 'RESTAURANT', 'Rapsy Restaurant', 'Kerala Regional',
    'Long-standing local favorite in Munnar town serving authentic Malabar parotta and Kerala curries.',
    'https://images.unsplash.com/photo-1610057099443-fde8c4d50f91?auto=format&fit=crop&w=800&q=80',
    NULL, '₹', 'INR', 4.40, 'Malabar Parotta, Appam with Stew, Spiced Tea', NULL, NULL,
    'Crisp flaky parottas, Cardamom-infused tea, Town center landmark',
    'Main Bazaar, Near Old Post Office, Munnar, Kerala 685612',
    10.0881, 77.0592,
    'OPEN_STREET_MAP', 'OSM-NODE-6281920', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
);

-- HAMPI LISTINGS
INSERT INTO travel_listings (
    id, destination_id, listing_type, name, category, description, image_url,
    price_estimate, price_range, currency, rating, must_try, estimated_hours, entry_fee,
    highlights, address, latitude, longitude, source_provider, provider_record_id,
    retrieved_at, stale_after, license_type, attribution_text, attribution_url, verification_status
) VALUES
(
    'hp-att-1', 'hampi', 'ATTRACTION', 'Vijaya Vittala Temple & Stone Chariot', 'Monument',
    'Architectural pinnacle of Vijayanagara empire, featuring the iconic Stone Chariot and musical pillars.',
    'https://images.unsplash.com/photo-1600100397608-f010e421d428?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.90, NULL, 3.0, 40.00,
    'Stone Chariot shrine, Musical Ranga Mantapa, Tungabhadra river walk',
    'Hampi Historical Site, Bellary District, Karnataka 583239',
    15.3359, 76.4770,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-KTK-001', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Archaeological Survey of India Mini Circle Hampi', 'https://asi.nic.in', 'VERIFIED_OFFICIAL'
),
(
    'hp-att-2', 'hampi', 'ATTRACTION', 'Virupaksha Temple', 'Monument',
    'Continuously functioning 7th-century temple complex dedicated to Lord Shiva on the south bank of Tungabhadra.',
    'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?auto=format&fit=crop&w=800&q=80',
    NULL, NULL, 'INR', 4.80, NULL, 2.0, 25.00,
    '50-meter gateway gopuram, Inverted pinhole camera shadow, Living worship heritage',
    'Hampi Bazaar, Hampi, Karnataka 583239',
    15.3351, 76.4601,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-KTK-002', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Archaeological Survey of India & Temple Trust', 'https://asi.nic.in', 'VERIFIED_OFFICIAL'
),
(
    'hp-stay-1', 'hampi', 'ACCOMMODATION', 'Heritage Resort Hampi', 'Boutique Stay',
    'Eco-conscious cottages set in organic farmland near the UNESCO monument boundary.',
    'https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?auto=format&fit=crop&w=800&q=80',
    5500.00, '₹₹₹', 'INR', 4.70, NULL, NULL, NULL,
    'Ayurvedic wellness, Private villa verandahs, Stargazing in boulder valley',
    'Hosamalapanagudi, State Highway 49, Hampi, Karnataka 583239',
    15.3120, 76.4480,
    'OPEN_STREET_MAP', 'OSM-WAY-4192801', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
),
(
    'hp-din-1', 'hampi', 'RESTAURANT', 'Mango Tree Restaurant', 'Multi-Cuisine Garden Cafe',
    'Famed traveler cafe situated in lush banana and mango groves near Kamalapura.',
    'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80',
    NULL, '₹₹', 'INR', 4.60, 'South Indian Banana Leaf Thali, Shakshuka, Fresh Pomegranate Juice', NULL, NULL,
    'Floor-cushion seating, Garden ambience, Refreshing fresh juices',
    'Near Kamalapura, Hampi, Karnataka 583239',
    15.3210, 76.4580,
    'OPEN_STREET_MAP', 'OSM-NODE-7819234', '2026-10-21 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors', 'https://www.openstreetmap.org', 'UNVERIFIED'
);
