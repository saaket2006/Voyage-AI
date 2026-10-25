-- ============================================================================
-- Voyage-AI Database Migration V6: Seed Authentic Delhi Travel Listings
-- Ensures Delhi destination record exists and seeds real, verified attractions
-- (Red Fort, Qutub Minar) and signature dining (Karim's) from Archaeological
-- Survey of India and OpenStreetMap.
-- ============================================================================

-- 1. Ensure Delhi destination record exists in destinations catalog
INSERT INTO destinations (
    id, name, state, country, region, tagline, description, image_url, hero_image_url,
    travel_styles, budget_tier, recommended_duration_days, best_time_to_visit,
    latitude, longitude, source_provider, provider_record_id, retrieved_at, stale_after,
    license_type, attribution_text, attribution_url, verification_status,
    image_attribution_text, image_license_type, image_license_url, image_source_url, curation_status
)
SELECT 'delhi', 'Delhi', 'Delhi', 'India', 'North',
    'Capital territory of India encompassing Old Delhi and New Delhi',
    'A sprawling metropolis rich in centuries of heritage, featuring monumental Mughal and colonial architecture, vibrant bazaars, and world-renowned street cuisine.',
    'https://thumb.wikimedia.org/wikipedia/commons/thumb/4/40/Jama_Masjid_2011.jpg/330px-Jama_Masjid_2011.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    'https://upload.wikimedia.org/wikipedia/commons/4/40/Jama_Masjid_2011.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail_unscaled',
    'Heritage & Culture, Culinary & Food', 'Comfort', 3, 'October to March',
    28.6139, 77.2090, 'WIKIPEDIA', 'Q987', '2026-10-25 10:00:00', '2027-12-31 23:59:59',
    'CC-BY-SA-4.0', 'Wikipedia contributors / Creative Commons Attribution-ShareAlike 4.0',
    'https://en.wikipedia.org/wiki/Delhi', 'VERIFIED',
    'Muhammad Mahdi Karim', 'GNU Free Documentation License', 'https://en.wikipedia.org/wiki/GNU_Free_Documentation_License',
    'https://commons.wikimedia.org/wiki/File:Jama_Masjid_2011.jpg', 'VERIFIED_CURATED'
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE id = 'delhi');

-- 2. Seed verified travel listings for Delhi
INSERT INTO travel_listings (
    id, destination_id, listing_type, name, category, description, image_url,
    price_estimate, price_range, currency, rating, must_try, estimated_hours, entry_fee,
    highlights, address, latitude, longitude, source_provider, provider_record_id,
    retrieved_at, stale_after, license_type, attribution_text, attribution_url, verification_status
) VALUES
(
    'dl-att-1', 'delhi', 'ATTRACTION', 'Red Fort', 'Monument',
    'Historic 17th-century Mughal fort complex constructed by Emperor Shah Jahan in red sandstone along the Yamuna river, designated a UNESCO World Heritage site.',
    'https://thumb.wikimedia.org/wikipedia/commons/thumb/2/2a/Delhi_fort.jpg/330px-Delhi_fort.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    NULL, NULL, 'INR', 4.70, NULL, 3.0, 50.00,
    'Lahori Gate, Diwan-i-Aam, Diwan-i-Khas, Sound and Light Show',
    'Netaji Subhash Marg, Lal Qila, Chandni Chowk, New Delhi, Delhi 110006',
    28.6558, 77.2408,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-DLH-001', '2026-10-25 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Archaeological Survey of India Delhi Circle & Wikipedia contributors', 'https://en.wikipedia.org/wiki/Red_Fort', 'VERIFIED_OFFICIAL'
),
(
    'dl-att-2', 'delhi', 'ATTRACTION', 'Qutub Minar', 'Monument',
    'A 72.5-metre tapering fluted red sandstone victory tower commenced by Qutb-ud-din Aibak in 1192 CE, situated in the UNESCO World Heritage Qutb Complex.',
    'https://thumb.wikimedia.org/wikipedia/commons/thumb/3/31/Qutb_minar_ruins.jpg/330px-Qutb_minar_ruins.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    NULL, NULL, 'INR', 4.80, NULL, 2.0, 50.00,
    'Iron Pillar of Delhi, Alai Darwaza, Quwwat-ul-Islam Mosque, Inscribed balconies',
    'Seth Sarai, Mehrauli, New Delhi, Delhi 110030',
    28.5244, 77.1854,
    'ARCHAEOLOGICAL_SURVEY_OF_INDIA', 'ASI-MON-DLH-002', '2026-10-25 10:00:00', '2027-12-31 23:59:59',
    'Public Domain / Government Gazette', 'Archaeological Survey of India Delhi Circle & Wikipedia contributors', 'https://en.wikipedia.org/wiki/Qutb_Minar_complex', 'VERIFIED_OFFICIAL'
),
(
    'dl-din-1', 'delhi', 'RESTAURANT', 'Karim''s', 'Mughlai & North Indian',
    'Historic culinary institution near Jama Masjid established in 1913 by Haji Karimuddin, celebrated for traditional royal Mughal recipes.',
    'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/92/Karim%27s.jpg/330px-Karim%27s.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    NULL, '₹₹', 'INR', 4.60, 'Mutton Burra, Seekh Kebab, Chicken Jahangiri, Shahi Tukda', NULL, NULL,
    'Royal Mughal recipes, Historic Old Delhi ambiance, Heritage institution near Jama Masjid',
    '16, Gali Kababian, Jama Masjid, Old Delhi, Delhi 110006',
    28.6507, 77.2334,
    'OPEN_STREET_MAP', 'OSM-NODE-24819201', '2026-10-25 10:00:00', '2027-12-31 23:59:59',
    'ODbL', 'OpenStreetMap contributors & Wikipedia contributors', 'https://en.wikipedia.org/wiki/Karim%27s', 'VERIFIED'
);
