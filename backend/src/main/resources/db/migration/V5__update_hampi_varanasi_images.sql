-- ============================================================================
-- Voyage-AI Database Migration V5: Fix Destination Image URLs and Provenance
-- Updates Hampi and Varanasi with verified, authentic Wikimedia Commons image
-- URLs and preserves accurate photographer attribution and Creative Commons licensing.
-- ============================================================================

-- 1. Hampi: Virupaksha Temple Galigopuram (Photo by iMahesh, CC BY-SA 4.0)
UPDATE destinations
SET image_url = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/dd/Wide_angle_of_Galigopuram_of_Virupaksha_Temple%2C_Hampi_%2804%29_%28cropped%29.jpg/330px-Wide_angle_of_Galigopuram_of_Virupaksha_Temple%2C_Hampi_%2804%29_%28cropped%29.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    hero_image_url = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/dd/Wide_angle_of_Galigopuram_of_Virupaksha_Temple%2C_Hampi_%2804%29_%28cropped%29.jpg/3840px-Wide_angle_of_Galigopuram_of_Virupaksha_Temple%2C_Hampi_%2804%29_%28cropped%29.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    image_attribution_text = 'iMahesh',
    image_license_type = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url = 'https://commons.wikimedia.org/wiki/File:Wide_angle_of_Galigopuram_of_Virupaksha_Temple,_Hampi_(04)_(cropped).jpg',
    curation_status = 'VERIFIED_CURATED',
    verification_status = 'VERIFIED'
WHERE id = 'hampi';

-- 2. Varanasi: Ghats cremation ceremony along the Ganges (Photo by Vyacheslav Argenberg, CC BY 4.0)
UPDATE destinations
SET image_url = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0e/Varanasi%2C_India%2C_Ghats%2C_Cremation_ceremony_in_progress.jpg/330px-Varanasi%2C_India%2C_Ghats%2C_Cremation_ceremony_in_progress.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail',
    hero_image_url = 'https://upload.wikimedia.org/wikipedia/commons/0/0e/Varanasi%2C_India%2C_Ghats%2C_Cremation_ceremony_in_progress.jpg?utm_source=en.wikipedia.org&utm_campaign=api&utm_content=thumbnail_unscaled',
    image_attribution_text = 'Vyacheslav Argenberg',
    image_license_type = 'CC BY 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by/4.0',
    image_source_url = 'https://commons.wikimedia.org/wiki/File:Varanasi,_India,_Ghats,_Cremation_ceremony_in_progress.jpg',
    curation_status = 'VERIFIED_CURATED',
    verification_status = 'VERIFIED'
WHERE id = 'varanasi';
