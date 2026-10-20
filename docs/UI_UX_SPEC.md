# Voyage-AI — UI/UX Specification

**Version:** 0.1 · **Status:** Initial design brief

## 1. Design objective

Create a polished travel product that feels editorial, calm, trustworthy, and useful—not a generic admin dashboard. Visual quality must support discovery and decision-making rather than compete with it.

The team has initialized Impeccable and Emil Kowalski design skills. Use them during design exploration, implementation critique, and refinement. Read the installed skill instructions before invoking their workflows; do not assume their exact command syntax.

## 2. Experience principles

- Lead with destination imagery and clear search.
- Keep search, filters, and trip context understandable.
- Make each listing actionable: view details, save, or add to trip.
- Show meaningful data, not decorative dashboard metrics.
- Use restrained animation only where it clarifies state or adds feedback.
- Maintain a coherent design language across discovery and itinerary pages.
- Provide responsive layouts, accessible contrast, keyboard interaction, and visible focus states.

## 3. Proposed sitemap

Public:
- Landing / Discover
- Destination search results
- Destination details
- Accommodation results/details
- Restaurant results/details
- Attractions and activities
- Sign in / Register

Authenticated:
- My Trips
- Create Trip
- Trip Workspace / Overview
- Itinerary Builder
- Budget Summary
- Saved Items (if included)
- Profile and Preferences

## 4. Key screen requirements

### Landing / Discover
- Strong destination-led hero with search entry point.
- Clear primary action: explore or plan a trip.
- Curated destination collections from real seed/provider data.
- Responsive card layout and visible loading/empty/error states.

### Search results
- Search query visible and editable.
- Filters limited to actual supported data.
- Sorting and result count where meaningful.
- Cards with consistent image ratios, location, summary, and available price/category context.
- Clear no-results recovery.

### Destination details
- High-quality hero image and concise overview.
- Useful highlights and location context.
- Related stays, restaurants, attractions, and activities.
- Actions to save/shortlist or start a trip with this destination.

### Listing details
- Clear title, image, location, category, description, available attributes, and source/price caveats.
- Add to trip/shortlist action when signed in.
- External link clearly labelled as external.

### Trip creation
- Short, progressive form for destination/preference, dates, traveler count, budget/currency, and interests.
- Inline validation and helpful defaults.
- Do not ask for optional details before they are useful.

### Itinerary builder
- Day navigation and clear day-by-day schedule.
- Add from discovered listings or create a custom activity.
- Edit, reorder, remove, and save items.
- AI-generated content visually identified as a proposal.
- Warnings for conflicts only when supported by data.
- Unsaved-change and generation-in-progress states.

### My Trips
- Clear upcoming/past/draft grouping if useful.
- Trip cards with destination, dates, and status.
- Helpful empty state that starts trip creation.

### Budget
- Target budget and currency.
- Known estimated costs grouped by category.
- Unknown values visibly excluded from totals.
- Clear caveat that estimates are not confirmed quotes.

## 5. Design system to define before page implementation

Create tokens for:
- Color: primary, accent, background, surface, text, muted text, border, success, warning, error.
- Typography: display, heading, body, label, metadata.
- Spacing scale and layout widths.
- Radius and elevation.
- Breakpoints.
- Motion duration/easing and reduced-motion behavior.
- Focus ring and accessible interaction states.

Avoid locking exact colors or fonts until a deliberate visual direction is selected. Keep tokens centralized.

## 6. Component inventory

- App shell and responsive navigation.
- Search bar and filter controls.
- Destination card.
- Accommodation/restaurant/attraction card variants.
- Image gallery/hero.
- Price estimate/source indicator.
- Rating display only when supported by source data.
- Button, input, select, date picker, budget input.
- Tabs/day selector.
- Itinerary item and draggable/reorderable list if implemented accessibly.
- Dialog/drawer, toast, confirmation.
- Skeleton/loading state.
- Empty state and error state.
- Responsive map/list split if map integration is included.

## 7. Interaction states

Every data-driven screen should consider:
- Initial loading.
- Partial loading.
- Empty result.
- Validation error.
- API/provider failure.
- Successful save.
- Unsaved changes.
- Authentication required.
- Unauthorized resource.
- AI generating.
- AI unavailable/invalid output.
- Reduced-motion preference.

## 8. Responsive behavior

- Mobile: single-column discovery, compact navigation, itinerary day selector, accessible bottom sheet/drawer where suitable.
- Tablet: adaptable card grid and compact side panels.
- Desktop: richer discovery layouts and optional itinerary/map split view.
- Avoid fixed widths that cause horizontal scrolling at common viewport sizes.

## 9. Design acceptance criteria

- All main pages use the same tokens and component patterns.
- Primary user journeys are possible by keyboard.
- Controls have visible focus and clear labels.
- Mobile layouts remain usable.
- Loading, empty, error, and success states are designed, not left to browser defaults.
- No placeholder button appears functional without an implemented action.
- No fake ratings, prices, availability, or testimonials are shown as factual data.
