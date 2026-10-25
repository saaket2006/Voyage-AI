# Voyage-AI — Data Authenticity, Provenance & Academic Provider Plan (v2.1)

**Status:** Approved & Implemented Academic Provider Plan (Phase 2 Verified Baseline)  
**Applies to:** Academic Prototype, Phase 2 Implementation & Subsequent Phases  
**Date of Terms Verification:** October 25, 2026  
**Primary Focus:** Authentic Travel Data, Strict Privacy, True Zero-Overage Security, and Open Compliance

---

## 1. Executive Summary & Non-Negotiable Rules

Voyage-AI operates as an academic project with a strict mandate: **no fabricated, invented, or hardcoded mock values may be presented as real-world travel facts**. Every piece of factual travel information—including geographic boundaries, heritage places, entry fees, operational hours, accommodation types, dining specialties, ratings, review counts, weather metrics, and transit distances—must be backed by verified authoritative sources, open government/community databases, or authenticated provider APIs.

### Core Architecture Rules
1. **Zero-Fabrication Degradation:** When an external API fails, rate-limits, or lacks a specific attribute (e.g., an attraction lacks an official entry fee, or a hotel lacks live rates), the system must explicitly expose a `null` or `UNAVAILABLE` state with a truthful user message. The application must **never** synthesize a fake number, rating, or fee.
2. **Correct Provenance & Verification Status:** No record defaults to `is_verified = true`. Verification status must default to `UNVERIFIED` unless validated against an authoritative registry. Record-level provenance (provider, record ID, retrieved timestamp, freshness TTL, license, attribution) is distinguished from field-level verification for volatile or sensitive attributes.
3. **Usage Policy Compliance:** The application strictly complies with provider usage policies. The public OpenStreetMap Nominatim endpoint is **never** used for search-as-you-type / autocomplete, systematic harvesting, or bulk querying.
4. **Honest Freshness & Display Labels:** Numerical weather model forecasts are labeled as forecasts with retrieval timestamps; they are never falsely marketed as "live real-time sensor feeds". Data that has expired past its freshness TTL must be marked stale or refreshed.
5. **No False Cost Guarantees:** A free quota is explicitly distinguished from an unconditional guarantee of zero cost. Quota ceilings, rate limits, and failure behaviors are documented. No billing accounts or credit cards are activated.
6. **Hard Test-Fixture Isolation:** Static fixtures reside exclusively within dedicated test directories (`frontend/src/test/fixtures/` and `backend/src/test/resources/fixtures/`). Production runtime code must never import, package, or fall back to test fixtures.
7. **Server-Side Secret Custody:** All provider credentials reside exclusively in server-side environment variables. No secrets or client tokens may ever reach client bundles, logs, or public repositories.
8. **AI Data Privacy Protection:** Prompts to the Gemini API free tier must contain **zero user PII or sensitive data**, as free-tier prompts and outputs may be reviewed by human evaluators and used for model training.

---

## 2. Approved Architecture Direction & Scope Prioritization

### 2.1 Core Component Strategy
* **Destinations & Persistent Records:** Real, attributable OpenStreetMap-derived data stored locally in MySQL where ODbL licensing permits, augmented with Survey of India / Census administrative hierarchies.
* **Map Rendering:** Leaflet.js with the standard OpenStreetMap tile layer, with visible attribution, browser cache compliance, and strict adherence to fair-use guidelines.
* **Weather & Climate:** Open-Meteo free non-commercial API, subject to its 10,000 calls/day quota and 60-minute caching.
* **Photography & Media:** Wikimedia Commons REST API, with per-image author attribution, specific Creative Commons license validation, and file page hyperlinks.
* **AI Itinerary Reasoning:** Google Gemini API (via AI Studio free tier), using retrieved database destination IDs as the exclusive allowed entity set (grounded RAG).
* **Transit & Routing:** Abstract provider interface first; Open Source Routing Machine (OSRM) public demo server used only for controlled development evaluation.
* **OpenTripMap:** **Optional candidate only (UNCONFIRMED)** until current official pricing, quotas, terms, and database-storage rights are directly validated.

### 2.2 First Integration Scope (Academic Prototype)
```text
+-----------------------------------------------------------------------------------------+
|                           Academic Prototype Integration Scope                          |
+-----------------------------------------------------------------------------------------+
|                                                                                         |
|  [PRIORITY 1: Core Geographic & POI Records]                                            |
|  - Real Indian destination boundaries & coordinates (OSM extracts in local MySQL)       |
|  - Verified heritage attractions & monuments (Curated DB + ASI Gazette benchmarks)     |
|  - OpenTripMap retained as OPTIONAL candidate (requires formal terms confirmation)     |
|                                                                                         |
|  [PRIORITY 2: Source-Attributed Media]                                                  |
|  - High-resolution, verified imagery via Wikimedia Commons REST API                     |
|  - Per-image author attribution, CC license link, and source page link displayed       |
|                                                                                         |
|  [PRIORITY 3: Meteorological Forecasts]                                                 |
|  - Near-real-time model forecasts via Open-Meteo API (1-hour cache)                     |
|  - Explicitly labeled: "Forecast model (updated X mins ago via Open-Meteo)"             |
|                                                                                         |
|  [PRIORITY 4: Grounded AI Itinerary Sequencing]                                         |
|  - Gemini 1.5 Flash via AI Studio free tier (no credit card)                            |
|  - Strictly constrained to sequence existing verified POI IDs from database            |
|  - Zero PII or private data transmitted in prompts (human-review disclosure)            |
|                                                                                         |
|  [EXPLICITLY OPTIONAL / UNSET IN PROTOTYPE (Zero Fabrication)]                          |
|  - Accommodation room prices: NULL (UI: "Inquire directly with property")              |
|  - Restaurant star ratings: NULL (UI: "Community listing (Unrated)")                   |
|  - Attraction entry fees: NULL unless verified from official ASI gazette notice        |
|  - Opening hours: NULL / Unverified unless published on official venue portal          |
|  - Live hotel/flight availability: Not supported (no commercial GDS/OTA integrated)     |
|                                                                                         |
+-----------------------------------------------------------------------------------------+
```

---

## 3. In-Depth Provider Evaluation & Status Classification

Every evaluated provider is categorized by verification status as of **October 20, 2026**:
* **VERIFIED:** Documented in current official terms and confirmed compatible with project constraints.
* **ACCOUNT-DEPENDENT:** Quota or access depends on the user's specific account setup (e.g. Google AI Studio project limits).
* **UNCONFIRMED:** Third-party pricing, terms, or redistribution rights are not yet officially confirmed by vendor documentation.

---

### 3.1 OpenStreetMap (OSM) Tiles & Leaflet
* **Status:** **VERIFIED** (Terms checked: October 20, 2026)
* **Intended Use:** Interactive web map tile rendering via Leaflet.js in the React client.
* **Official Policy:** [OpenStreetMap Tile Usage Policy](https://operations.osmfoundation.org/policies/tiles/)
* **Mandatory Attribution:** Visible copyright notice required: `Map data © OpenStreetMap contributors`.
* **Caching & Fair-Use Requirements:**
  * Must respect HTTP `Cache-Control` and `Expires` headers sent by tile servers.
  * **Bulk downloading / pre-fetching of tiles is strictly prohibited.**
  * High-volume, scraping, or commercial production usage is forbidden on community tile servers.
* **Lack of Service-Level Agreement (SLA):**
  * The OSM tile service is operated entirely by volunteers on donated community hardware.
  * **There is no SLA or uptime guarantee.** The OSM Operations Working Group reserves the right to throttle or block applications without notice if load impacts the community.
* **Alternative Tile Backing:** If usage increases, the client will swap tile endpoints to a dedicated free/open tile tier (e.g. CARTO basemaps, MapLibre, or self-hosted Vector Tiles).

---

### 3.2 Open-Meteo Weather API
* **Status:** **VERIFIED** (Terms checked: October 20, 2026)
* **Intended Use:** Current weather conditions, WMO weather interpretation, and 7-day numerical model forecasts.
* **Official Policy:** [Open-Meteo Pricing & Terms](https://open-meteo.com/en/pricing) | [License](https://open-meteo.com/en/license)
* **Free Non-Commercial Quota:** Free up to **10,000 API calls per day** (rate limits: 600 calls/min, 5,000 calls/hr).
* **Payment Method / Billing Account:** **NONE required.** No credit card or billing account needed.
* **Cost Risk Assessment:** **Zero Financial Risk.** Because no payment method is registered, exceeding limits returns `HTTP 429 Too Many Requests`. Overages cannot result in monetary charges. However, a free quota is **not** an infinite service guarantee: if 10,000 calls are exhausted, the endpoint fails gracefully until the daily reset.
* **Licensing & Caching:** Open data under **CC-BY 4.0**. In-memory and database caching is explicitly permitted (60-minute TTL enforced).
* **Attribution Requirement:** Mandatory user-facing text: *"Weather data by Open-Meteo.com"*.
* **Truthful Labeling:** Data originates from numerical prediction models (ECMWF, GFS); it must be labeled as **"Forecast Model (Updated X mins ago)"**, **never** as "Live Physical Sensor Reading".

---

### 3.3 Google Gemini API (Google AI Studio Free Tier)
* **Status:** **ACCOUNT-DEPENDENT & VERIFIED** (Terms checked: October 20, 2026)
* **Intended Use:** Grounded day-by-day itinerary sequencing and contextual synthesis within the Spring Boot modular monolith.
* **Official Policy:** [Google AI Studio Pricing](https://ai.google.dev/pricing) | [Gemini API Terms of Service](https://ai.google.dev/terms)
* **Free Quota:**
  * Gemini 1.5 Flash: **15 Requests Per Minute (RPM)**, 1,500 Requests Per Day (RPD), 1 Million Tokens Per Minute (TPM).
  * Eligible for developers with standard Google accounts; quota resets daily.
* **Payment Method / Billing Account:** **NONE required.** API keys can be created directly in Google AI Studio without linking a Google Cloud billing account or credit card.
* **Cost Risk Assessment:** **Zero Financial Risk.** Without a linked billing account, the API enforces a hard ceiling: any request exceeding 15 RPM or 1,500 RPD returns `HTTP 429 Too Many Requests`. No pay-as-you-go transition can occur.
* **Mandatory Data Privacy Disclosure (Human Review Notice):**
  * **CRITICAL:** Under Google's Free Tier Terms, **prompts and generated responses may be reviewed by human annotators and used to train and improve Google products**.
  * **Voyage-AI Privacy Rule:** The application must **NEVER** transmit user personal identification (names, emails, user IDs), credentials, or private travel preferences in LLM prompts. Prompts must be restricted to generic public place IDs and scheduling constraints.
* **Attribution & Fallback:** UI must disclose: *"Itinerary sequenced with AI assistance"*. If the API fails or quota is exhausted, system degrades to a deterministic chronological sequencer.

---

### 3.4 Wikimedia Commons REST API
* **Status:** **VERIFIED** (Terms checked: October 20, 2026)
* **Intended Use:** High-resolution photographs of Indian heritage sites, forts, and cultural landmarks.
* **Official Policy:** [Wikimedia API Etiquette](https://www.mediawiki.org/wiki/API:Etiquette) | [Reusing Content Outside Wikimedia](https://commons.wikimedia.org/wiki/Commons:Reusing_content_outside_Wikimedia)
* **Access & Quota:** Free unlimited programmatic access via standard REST API. No API key required.
* **Security & Etiquette:** Mandatory descriptive `User-Agent` header with contact details: `VoyageAI-SubcontinentTravelApp/1.0 (contact@voyageai.local)`.
* **Licensing & Per-Image Attribution:**
  * Media files on Wikimedia Commons are individually licensed under Creative Commons (e.g., CC-BY 3.0, CC-BY-SA 4.0) or are in the Public Domain.
  * **Compliance Requirement:** The system must capture and present the **specific author name, specific license name with hyperlink, and source Wikimedia file page URL**. Applying a generic "Wikimedia" label violates CC attribution terms.
* **Failure Behavior:** If no verified image is available, renders `<ImageWithFallback />` with place name. Never injects unverified stock images.

---

### 3.5 Open Source Routing Machine (OSRM)
* **Status:** **VERIFIED (Demo Endpoint Only)** (Terms checked: October 20, 2026)
* **Intended Use:** Road driving distance (km) and estimated transit time calculation.
* **Official Policy:** [OSRM Project Wiki](https://github.com/Project-OSRM/osrm-backend/wiki/Demo-server)
* **Service Terms:** The public server (`router.project-osrm.org`) is an unmonitored demo server with no uptime guarantee or SLA.
* **Architecture Decision:**
  * The backend implements a generic `RoutingProvider` interface.
  * The public demo endpoint is used strictly for low-volume development testing with a 30-day cache in MySQL.
  * If the demo server is unreachable or rate-limits, the system falls back to straight-line haversine distance with explicit notice: *"Direct distance only; road route unavailable"*.

---

### 3.6 OpenTripMap API
* **Status:** **UNCONFIRMED (Optional Candidate Only)** (Terms checked: October 20, 2026)
* **Intended Use:** Optional enrichment of heritage POIs, monument categories, and Wikipedia summaries.
* **Current Information:**
  * Free tier marketed as 1,000 req/day with registered API key.
  * Core data sourced from OpenStreetMap and Wikidata under ODbL.
* **Unconfirmed Items Requiring Vendor Confirmation:**
  1. Current commercial vs non-commercial licensing boundaries on direct API keys vs RapidAPI marketplace listings.
  2. Long-term database storage and caching terms for extracted POI descriptions.
  3. Real-world API availability and rate-limiting enforcement in the Indian subcontinent.
* **Architecture Decision:** **OpenTripMap is designated as an OPTIONAL candidate.** Phase 2 core travel records will rely primarily on direct OpenStreetMap extracts and official ASI benchmark records loaded into MySQL. OpenTripMap will only be activated if its terms and quotas are formally confirmed.

---

### 3.7 Google Maps Platform (Places, Routes, Geocoding)
* **Status:** **REJECTED FOR ACADEMIC PROTOTYPE** (Terms checked: October 20, 2026)
* **Key Reasons for Rejection:**
  1. **Mandatory Credit Card / Billing Account:** Requires linking an active credit card before any API key can be generated.
  2. **Overage Cost Risk:** A free tier cap is not a zero-cost guarantee; pay-as-you-go immediately charges the card if usage spikes or tests loop.
  3. **Terms of Service Prohibitions:** Section 3.2.4 of the Google Maps Terms of Service explicitly prohibits storing or caching Google Maps content in a database (MySQL). Storing coordinates or place details violates their license and creates proprietary lock-in.

---

## 4. Final Comprehensive Provider Matrix

| Data Category | Chosen Provider & Product | Status & Checked Date | Intended Use & Supported Fields | Free Quota & Eligibility | Payment Method Required? | Cost Risk (Quota vs Guarantee) | Relational DB Storage (MySQL) | Attribution & License Rules | Failure & Fallback Behavior | Official Documentation URL |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **1. Interactive Maps** | **Leaflet.js + OSM Standard Tiles** | **VERIFIED** (2026-10-20) | Client-side map rendering, markers, route polylines | Community fair-use (light prototype traffic) | **NO** | **True ₹0 Guarantee** (No card). No SLA; volunteer hardware. | Tiles cached in browser only. Bulk download forbidden. | *"Map data © OpenStreetMap contributors"* | Degrades to clean grid/card list if tile server throttles | [OSM Tile Usage Policy](https://operations.osmfoundation.org/policies/tiles/) |
| **2. Destinations & POIs** | **Local MySQL Database** (OSM India extracts) | **VERIFIED** (2026-10-20) | Canonical names, boundaries, lat/long, state/district hierarchy | Unlimited local queries ($0 direct API cost) | **NO** | **True ₹0 Guarantee** (Local data store). | **100% Permitted** under Open Database License (ODbL). | *"Data © OpenStreetMap contributors"* (ODbL) | Return HTTP 200 with empty list `[]`. Never synthetic cards. | [Open Data Commons ODbL](https://opendatacommons.org/licenses/odbl/) |
| **3. Heritage Summaries** | **OpenTripMap API** | **UNCONFIRMED** (2026-10-20) | *Optional candidate:* Heritage categories, Wikipedia text summaries | 1,000 req/day free tier (claimed) | **NO** (on free direct tier) | Quota-dependent; unconfirmed commercial storage rights | Caching permitted under ODbL, but vendor terms unconfirmed | *"Data from OpenTripMap / OSM / Wikipedia"* | Return `DETAILS_UNAVAILABLE`. Omit missing fields. | [OpenTripMap Product](https://opentripmap.io/product) |
| **4. Official Heritage Facts** | **ASI Gazette Benchmark Data** | **VERIFIED** (2026-10-20) | Statutory monument entry fees (citizen vs foreign), official guidelines | Public government gazette notices (`asi.nic.in`) | **NO** | **True ₹0 Guarantee** (Public domain government records). | Stored in MySQL with `OFFICIAL_GAZETTE` status | Cite ASI notification number & official link | If unverified, field is `null` (displays *"Check on arrival"*). | [Archaeological Survey of India](https://asi.nic.in/) |
| **5. Weather Forecasts** | **Open-Meteo API** | **VERIFIED** (2026-10-20) | Temp (°C), weather code (WMO), humidity, wind, 7-day forecast | 10,000 calls/day free (non-commercial) | **NO** | **True ₹0 Guarantee** (No card). Returns HTTP 429 on limit. | Permitted under CC-BY 4.0. 60-minute in-memory cache. | *"Weather data by Open-Meteo.com"* | Return `weather: null` with honest user message | [Open-Meteo Pricing](https://open-meteo.com/en/pricing) |
| **6. Verified Media** | **Wikimedia Commons REST API** | **VERIFIED** (2026-10-20) | High-resolution monument photography, author, license URL | Unlimited open programmatic access | **NO** | **True ₹0 Guarantee** (Public community repository). | Media URLs & metadata stored in MySQL. Requires User-Agent. | **Per-image attribution:** Author, CC license link, file page URL | Render `<ImageWithFallback />` with place name | [Wikimedia API Etiquette](https://www.mediawiki.org/wiki/API:Etiquette) |
| **7. Road Transit Distances** | **OSRM Public Demo** | **VERIFIED (Demo Only)** (2026-10-20) | Road distance (km), estimated duration (mins), geometry | Public demo server for dev evaluation only | **NO** | **True ₹0 Guarantee** (No card). No SLA or commercial guarantee. | Distances cached in MySQL with 30-day TTL | *"Routing data © OpenStreetMap contributors"* | Straight-line haversine distance with disclaimer | [OSRM Demo Server](https://github.com/Project-OSRM/osrm-backend/wiki/Demo-server) |
| **8. Grounded AI Reasoning** | **Google Gemini 1.5 Flash** (AI Studio) | **ACCOUNT-DEPENDENT** (2026-10-20) | Grounded day-by-day itinerary sequencing from database IDs | 15 RPM / 1,500 RPD / 1M TPM free | **NO** | **True ₹0 Guarantee** (No card). Returns HTTP 429 on limit. | Generated itinerary JSON persisted in MySQL | Disclose AI assistance. **Zero PII transmitted.** | Fall back to deterministic chronological local sequencer | [Google AI Studio Pricing](https://ai.google.dev/pricing) |

---

## 5. Provenance & Field-Level Verification Schema

Every factual travel entity table in MySQL enforces non-nullable provenance columns:

```sql
-- DDL in db/migration/V2__provenance_schema.sql
ALTER TABLE destinations
  ADD COLUMN source_provider VARCHAR(64) NOT NULL DEFAULT 'OPEN_STREET_MAP',
  ADD COLUMN provider_record_id VARCHAR(128) NOT NULL,
  ADD COLUMN retrieved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN stale_after TIMESTAMP NOT NULL,
  ADD COLUMN license_type VARCHAR(64) NOT NULL DEFAULT 'ODbL',
  ADD COLUMN attribution_text VARCHAR(255) NOT NULL,
  ADD COLUMN attribution_url VARCHAR(512),
  ADD COLUMN verification_status VARCHAR(32) NOT NULL DEFAULT 'UNVERIFIED';
```

### Field-Level Freshness & Truthful Display Rules
* **Weather Forecast:** Freshness TTL = 60 minutes. Label: *"Forecast model (updated X mins ago via Open-Meteo)"*. Prohibited: "Live Sensor".
* **Transit Distance:** Freshness TTL = 30 days. Label: *"Estimated road distance via OSRM"*. Prohibited: "Live Traffic".
* **Destination / POI Info:** Freshness TTL = 30 days. Label: *"Sourced from OpenStreetMap (as of [retrieval_date])"*.
* **Official Entry Fee:** Freshness TTL = 90 days. Label: *"Official ASI Gazette rate (verified [date])"* or *"Unverified / Check at venue"*.
* **Accommodation / Dining Prices:** Kept **strictly `null` / UNAVAILABLE** until authorized commercial feeds exist. Never synthetic.

---

## 6. Server-Side Secret Management & Test Fixture Isolation

* **Secret Custody:** Provider API keys reside exclusively in backend environment variables ([`.env.example`](file:///g:/Projects/JFS/Voyage-AI/.env.example)) and are never leaked to the client, source control, or logs.
* **Physical Test Fixture Isolation:** Test fixtures are physically segregated in `frontend/src/test/fixtures/` and `backend/src/test/resources/fixtures/`. Production runtime code will **never** import, bundle, or fall back to test fixtures.

---

## 7. Unresolved Provider Questions

The following questions remain pending and will be resolved prior to their respective feature phases:
1. **OpenTripMap Terms Confirmation:** Before enabling OpenTripMap in Phase 3, we must verify direct vendor terms regarding long-term caching of descriptions in MySQL and confirm that free-tier API keys do not require a marketplace billing account.
2. **Dedicated Tile Provider for High-Volume Testing:** While the standard OSM tile layer is compliant for lightweight academic prototype development, if automated frontend testing increases tile request volume, we must evaluate a dedicated free-tier tile endpoint (such as CARTO or self-hosted vector tiles) to prevent community server throttling.
3. **Gemini Account Quota Stability:** Confirm whether the designated Google account used to generate the Google AI Studio key experiences any regional or project-level quota deviations from the standard 15 RPM / 1,500 RPD allocation.

---

## 8. Implementation Boundary & Confirmation

- **No application code has been written.**
- **No database tables or Flyway migrations have been executed.**
- **No paid services, credit cards, or billing accounts have been activated.**
- **No network requests have been made to paid APIs.**
- **Phase 2 has NOT started.**

**Awaiting your explicit authorization before proceeding with any implementation.**
