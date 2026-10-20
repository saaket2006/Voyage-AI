package com.voyageai.tests.browser;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.voyageai.tests.base.BasePlaywrightTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Destinations Discovery and Filtering Tests")
public class DestinationsDiscoveryTest extends BasePlaywrightTest {

    @BeforeEach
    void navigateToDiscovery() {
        page.navigate("/");
    }

    @Test
    @DisplayName("Renders destination cards with titles, badges, and pricing")
    void testRendersDestinationCards() {
        // Assert cards are displayed using accessible role selectors
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).isVisible();
        assertThat(page.locator("text=The Pink City of Royal Fortresses").first()).isVisible();

        // Verify weather chip format
        assertThat(page.locator("text=°C").first()).isVisible();

        // Verify illustrative estimate label is visible
        assertThat(page.locator("text=/day").first()).isVisible();
    }

    @Test
    @DisplayName("Filters destinations by region (North India vs South India)")
    void testFiltersDestinationsByRegion() {
        // Click 'North India' filter
        Locator northIndiaBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("North India").setExact(true));
        northIndiaBtn.click();
        assertThat(northIndiaBtn).hasAttribute("aria-pressed", "true");

        // Jaipur should be visible, Hampi should NOT be visible
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).isVisible();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Hampi"))).not().isVisible();

        // Click 'South India' filter
        Locator southIndiaBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("South India").setExact(true));
        southIndiaBtn.click();
        assertThat(southIndiaBtn).hasAttribute("aria-pressed", "true");

        // Hampi should be visible, Jaipur should NOT be visible
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Hampi"))).isVisible();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).not().isVisible();
    }

    @Test
    @DisplayName("Filters destinations by search query in real time")
    void testFiltersDestinationsBySearchQuery() {
        Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE)));

        // Search for "Munnar"
        searchInput.fill("Munnar");

        // Munnar card is visible, Jaipur is filtered out
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Munnar"))).isVisible();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).not().isVisible();

        // Clear search query
        searchInput.clear();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).isVisible();
    }

    @Test
    @DisplayName("Resets all active filters when Reset filters button is clicked")
    void testResetsAllActiveFilters() {
        // Apply region filter
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Himalayas").setExact(true)).click();

        // Reset filters button should become visible
        Locator resetBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("reset filters", Pattern.CASE_INSENSITIVE)));
        assertThat(resetBtn).isVisible();

        resetBtn.click();

        // All destinations should be visible again
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).isVisible();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Hampi"))).isVisible();
    }

    @Test
    @DisplayName("Displays empty state when search finds no matches and allows recovery")
    void testDisplaysEmptyStateAndRecovers() {
        Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE)));
        searchInput.fill("NonExistentAtlantisDestination123");

        // Empty state heading should appear
        assertThat(page.locator("text=No destinations found")).isVisible();

        // Click the reset action button inside EmptyState
        Locator resetBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("reset all filters", Pattern.CASE_INSENSITIVE)));
        assertThat(resetBtn).isVisible();
        resetBtn.click();

        // Cards should reappear
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).isVisible();
    }
}
