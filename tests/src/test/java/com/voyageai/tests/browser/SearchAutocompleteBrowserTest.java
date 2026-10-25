package com.voyageai.tests.browser;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.voyageai.tests.base.BasePlaywrightTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Global Search Bar Real-Time Autocomplete Tests")
public class SearchAutocompleteBrowserTest extends BasePlaywrightTest {

    private Locator searchInput;

    @BeforeEach
    void setUp() {
        page.navigate("/");
        searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE)));
        assertThat(searchInput).isVisible();
    }

    @Test
    @DisplayName("Displays grouped suggestions in strict order: Cities -> Attractions -> Dining for Delhi")
    void testSearchSuggestionsGroupedOrderDelhi() {
        // Type 'Delhi' to trigger real-time suggestions
        searchInput.fill("Delhi");

        // Autocomplete dropdown should appear directly below search bar
        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        // Verify group ordering: Cities -> Attractions -> Dining
        Locator groups = dropdown.locator("[role=\"group\"]");
        assertThat(groups.count()).isEqualTo(3);

        // Group 1: Cities
        Locator citiesGroup = groups.nth(0);
        assertThat(citiesGroup).hasAttribute("aria-label", "Cities");
        assertThat(citiesGroup.locator("text=Cities").first()).isVisible();
        assertThat(citiesGroup.locator("text=Delhi").first()).isVisible();

        // Group 2: Attractions
        Locator attractionsGroup = groups.nth(1);
        assertThat(attractionsGroup).hasAttribute("aria-label", "Attractions");
        assertThat(attractionsGroup.locator("text=Attractions").first()).isVisible();
        assertThat(attractionsGroup.locator("text=Red Fort").first()).isVisible();

        // Group 3: Dining
        Locator diningGroup = groups.nth(2);
        assertThat(diningGroup).hasAttribute("aria-label", "Dining");
        assertThat(diningGroup.locator("text=Dining").first()).isVisible();
        assertThat(diningGroup.locator("text=Karim's").first()).isVisible();

        // Verify location details and metadata are present
        assertThat(citiesGroup.locator("text=India").first()).isVisible();
    }

    @Test
    @DisplayName("Displays grouped suggestions for Jaipur with verified real data")
    void testSearchSuggestionsGroupedOrderJaipur() {
        searchInput.fill("Jaipur");

        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        Locator groups = dropdown.locator("[role=\"group\"]");
        assertThat(groups.count()).isGreaterThanOrEqualTo(2);

        // Group 1: Cities (Jaipur)
        Locator citiesGroup = groups.nth(0);
        assertThat(citiesGroup).hasAttribute("aria-label", "Cities");
        assertThat(citiesGroup.locator("text=Jaipur").first()).isVisible();

        // Verify Attractions or Dining for Jaipur
        assertThat(dropdown.locator("text=Amber Fort & Palace").or(dropdown.locator("text=Hawa Mahal")).first()).isVisible();
        assertThat(dropdown.locator("text=Laxmi Mishthan Bhandar").or(dropdown.locator("text=Dining")).first()).isVisible();
    }

    @Test
    @DisplayName("Supports keyboard navigation (ArrowDown, ArrowUp, Enter) to select result")
    void testKeyboardNavigationAndSelection() {
        searchInput.fill("Delhi");

        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        // Press ArrowDown to highlight first suggestion (Delhi)
        searchInput.press("ArrowDown");
        Locator delhiOption = page.locator("#suggestion-delhi");
        assertThat(delhiOption).hasAttribute("aria-selected", "true");

        // Press ArrowDown again to highlight second suggestion (Qutub Minar)
        searchInput.press("ArrowDown");
        Locator secondOption = page.locator("#suggestion-dl-att-2");
        assertThat(secondOption).hasAttribute("aria-selected", "true");

        // Press ArrowDown again to highlight third suggestion (Red Fort)
        searchInput.press("ArrowDown");
        Locator thirdOption = page.locator("#suggestion-dl-att-1");
        assertThat(thirdOption).hasAttribute("aria-selected", "true");

        // Press Enter to select
        searchInput.press("Enter");

        // Dropdown closes after selection
        assertThat(dropdown).not().isVisible();

        // Modal opens with selected destination
        Locator modal = page.getByRole(AriaRole.DIALOG);
        assertThat(modal).isVisible();
        assertThat(modal.locator("text=Delhi").first()).isVisible();
    }

    @Test
    @DisplayName("Closes dropdown on Escape key")
    void testKeyboardEscapeClosesDropdown() {
        searchInput.fill("Delhi");

        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        // Press Escape
        searchInput.press("Escape");

        // Dropdown closes
        assertThat(dropdown).not().isVisible();
    }

    @Test
    @DisplayName("Closes dropdown on outside click")
    void testClickOutsideClosesDropdown() {
        searchInput.fill("Delhi");

        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        // Click outside on the hero heading
        page.locator("h1").click();

        // Dropdown closes
        assertThat(dropdown).not().isVisible();
    }

    @Test
    @DisplayName("Renders clear empty state when no matching results exist")
    void testEmptyStateForNonExistentQuery() {
        searchInput.fill("NonExistentPlaceXYZ999");

        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        // Verify empty state message
        assertThat(dropdown.locator("text=No results found for “NonExistentPlaceXYZ999”")).isVisible();
        assertThat(dropdown.locator("text=Try searching for a city like")).isVisible();
    }

    @Test
    @DisplayName("Clicking a suggestion navigates and opens destination details")
    void testClickResultNavigatesToDetails() {
        searchInput.fill("Delhi");

        Locator dropdown = page.locator("#autocomplete-dropdown");
        assertThat(dropdown).isVisible();

        // Click on Red Fort attraction
        Locator redFort = dropdown.locator("text=Red Fort").first();
        redFort.click();

        // Dropdown closes
        assertThat(dropdown).not().isVisible();

        // Modal opens
        Locator modal = page.getByRole(AriaRole.DIALOG);
        assertThat(modal).isVisible();
        assertThat(modal.locator("text=Delhi").first()).isVisible();

        // URL updated with route
        assertThat(page.url()).contains("/attractions/dl-att-1");
    }
}
