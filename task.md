# Voyage-AI — Project Design Context & Guidelines

## Design Context

### Users & Purpose
- **Target Audience:** Independent leisure travelers, friends, families, and budget-conscious explorers planning domestic and international trips (with initial curated focus on Indian destinations).
- **Core Job to be Done:** Seamlessly transition from destination discovery to curated exploration (stays, dining, attractions) into a persistent, editable day-by-day itinerary with transparent budget estimation.
- **Emotional Goals:** Calm, editorial, confident, inspiring, and transparent. The interface should feel like a premium travel publication rather than a sterile admin dashboard or noisy coupon aggregator.

### Brand Personality
- **Voice & Tone:** Editorial, Curated, Trustworthy.
- **Three-Word Personality:** *Editorial, Tactile, Honest.*
- **Visual Direction:** Warm modernism. High-quality editorial travel imagery, balanced typography, generous breathing room, and structured information hierarchy.
- **Anti-References:**
  - ❌ Generic corporate SaaS admin templates with cluttered dashboard widgets.
  - ❌ Flashing, urgent booking sites with fake countdown timers and pressure tactics ("3 people looking right now!").
  - ❌ Dark, neon-heavy gaming aesthetics.

### Aesthetic & Token Specifications
- **Theme:** Clean light mode baseline with high-contrast editorial hierarchy.
- **Color Palette:**
  - Primary Brand: Deep Voyager Navy (`#1e3a8a` / `hsl(222, 47%, 18%)`)
  - Accent: Warm Terracotta / Sunset Amber (`#ea580c` / `hsl(24, 94%, 50%)`)
  - Surface Background: Warm Slate Tint (`#f8fafc` / `hsl(210, 40%, 98%)`)
  - Surface Card: Pure White (`#ffffff`)
  - Borders: Subdued Slate (`#e2e8f0`)
  - Text: Slate 900 (`#0f172a`) for display/headings, Slate 600 (`#475569`) for body.
- **Typography:**
  - Display / Headings: `Outfit`, sans-serif (editorial geometric warmth).
  - Body & UI Controls: `Inter`, system-ui, sans-serif (crisp legibility).

### Design Principles (Impeccable & Emil Kowalski Alignment)
1. **Honest Data Provenance:** Never fabricate certainty. Clearly distinguish confirmed values from estimates, and always surface unknown costs rather than counting them as zero.
2. **Tactile Interaction & Micro-Feedback:** Buttons scale down (`transform: scale(0.97)`) on press (`:active`) with 160ms ease-out transitions. Popovers expand from triggers, never from random screen centers.
3. **Restrained, Intentional Motion:** UI animations strictly under 250–300ms using custom curves (`cubic-bezier(0.23, 1, 0.32, 1)`). Never animate keyboard shortcuts. Always honor `prefers-reduced-motion`.
4. **Actionable State Design:** Every data view explicitly handles four states: Initial Loading (fast skeletons), Empty Results (helpful calls to action), Success/Persistence, and Actionable Error Feedback.
5. **Accessible By Default:** WCAG 2.2 AA contrast compliance (minimum 4.5:1), visible 2px focus rings (`focus-visible:ring-2`), and complete keyboard navigation across all core user journeys.

---

## Phase 1 Implementation & Design Decisions

### 1. Component Inventory
- `src/components/common/Button.tsx`: Tactile button with 5 semantic variants, `:active:scale(0.97)`, visible focus ring, and loading state spinner.
- `src/components/common/Badge.tsx`: Compact semantic tag pills for regions, travel styles, and budget ratings.
- `src/components/common/SkeletonCard.tsx`: Accessible shimmer loading state respecting `prefers-reduced-motion`.
- `src/components/common/EmptyState.tsx`: High-craft empty state when search/filter returns 0 results with a 1-click reset action.
- `src/components/layout/Navbar.tsx`: Editorial header with brand identity, live backend health indicator, exploration category tabs, and search bar.
- `src/components/discovery/HeroSection.tsx`: Warm modernism editorial showcase with high-res photography, dark gradient overlays for WCAG 2.2 AA contrast, and value proposition pillars.
- `src/components/discovery/FilterBar.tsx`: Multi-attribute filters for Region, Travel Style, and Budget Tier with active counts and quick reset.
- `src/components/discovery/DestinationCard.tsx`: Editorial cards with climate badge, duration estimate, review count, tagline, stays & dining counters, and honest average daily budget.
- `src/components/discovery/DestinationDetailModal.tsx`: Accessible modal dialog (`role="dialog"`, ESC key dismissal, backdrop dismiss) with 5 tabs: Overview, Curated Stays, Dining & Cuisine, Attractions, and Honest Budget Breakdown.

### 2. Design Review Table (Emil Kowalski Philosophy Alignment)

| Before | After | Why |
| --- | --- | --- |
| Static Phase 0 foundation status dashboard | Curated editorial travel discovery prototype with Hero, Cards, & Filters | Elevates product from sterile admin dashboard to inspiring travel publication |
| Generic blue buttons without tactile feedback | `btn-tactile` with `:active:scale(0.97)` and 150ms `cubic-bezier(0.23,1,0.32,1)` | Interfaces feel alive and responsive when buttons physically yield to press |
| Hidden or omitted daily travel expenses | Honest daily budget tiers (Budget / Comfort / Luxury) + total for 2 travelers | Respects honest data provenance; builds user trust by eliminating surprise costs |
| Ad-hoc modal transitions | Restrained 200ms scale entrance (`0.96 -> 1`) with ESC key listener | Provides smooth spatial continuity without sluggish delays or jarring jumps |
| Default browser outline on focus | 2px accessible focus ring (`focus-visible:ring-2 focus-visible:ring-brand-600`) | WCAG 2.2 AA keyboard accessibility requirement |

