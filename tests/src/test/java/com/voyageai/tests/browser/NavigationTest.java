package com.voyageai.tests.browser;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.voyageai.tests.base.BasePlaywrightTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("App Navigation and Layout Tests")
public class NavigationTest extends BasePlaywrightTest {

    @Test
    @DisplayName("Launches successfully and renders brand header and navigation items on desktop")
    void testLaunchesSuccessfullyAndRendersBrandHeader() {
        page.navigate("/");

        // Verify brand identity
        assertThat(page.locator("text=Voyage-AI").first()).isVisible();
        assertThat(page.locator("text=Discovery").first()).isVisible();
        assertThat(page.locator("text=Curated Subcontinent Journeys").first()).isVisible();

        // Verify desktop main navigation items
        Locator nav = page.locator("nav[aria-label=\"Main Navigation\"]");
        assertThat(nav.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Destinations"))).isVisible();
        assertThat(nav.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Curated Stays"))).isVisible();
        assertThat(nav.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Signature Dining"))).isVisible();
        assertThat(nav.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Itinerary Planner"))).isVisible();

        // Verify system status indicator exists
        Locator statusBadge = page.locator("[title*=\"Backend REST API\"]");
        assertThat(statusBadge).isVisible();

        // Verify search input is present and accessible
        Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE)));
        assertThat(searchInput).isVisible();

        // Ensure no fatal uncaught JavaScript errors occurred on initial load
        List<String> fatalErrors = consoleErrors.stream()
                .filter(err -> !err.contains("favicon") && !err.contains("net::ERR_"))
                .toList();
        assertThat(fatalErrors).isEmpty();
    }

    @Test
    @DisplayName("Switches editorial tabs when clicked on desktop")
    void testSwitchesEditorialTabsWhenClickedOnDesktop() {
        page.navigate("/");

        Locator staysTab = page.locator("nav[aria-label=\"Main Navigation\"]")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Curated Stays"));
        staysTab.click();
        assertThat(staysTab).hasClass(Pattern.compile("bg-white"));

        Locator diningTab = page.locator("nav[aria-label=\"Main Navigation\"]")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Signature Dining"));
        diningTab.click();
        assertThat(diningTab).hasClass(Pattern.compile("bg-white"));

        Locator destinationsTab = page.locator("nav[aria-label=\"Main Navigation\"]")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Destinations"));
        destinationsTab.click();
        assertThat(destinationsTab).hasClass(Pattern.compile("bg-white"));
    }

    @Test
    @DisplayName("Renders clean header and search on mobile viewports without desktop tab bar")
    void testMobileNavigationShowsBrandAndSearch() {
        page = createMobilePage();
        page.navigate("/");

        // Brand logo and title should be visible
        assertThat(page.locator("text=Voyage-AI").first()).isVisible();
        assertThat(page.locator("text=Discovery").first()).isVisible();

        // Search input is accessible on mobile
        Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE)));
        assertThat(searchInput).isVisible();

        // Desktop nav should not be visible on mobile
        Locator nav = page.locator("nav[aria-label=\"Main Navigation\"]");
        assertThat(nav).not().isVisible();
    }

    @Test
    @DisplayName("Verifies shared footer retains logo, Spring Boot, MySQL and cleanly removes prototype and token labels")
    void testSharedFooterRendersCleanlyWithoutRemovedLabels() {
        page.navigate("/");

        Locator footer = page.locator("footer");
        assertThat(footer).isVisible();

        // Verify Voyage-AI logo and name in footer
        assertThat(footer.locator("text=Voyage-AI")).isVisible();

        // Verify Spring Boot and MySQL version and port details remain
        assertThat(footer.locator("text=Spring Boot 4.1.1 (Port 8081)")).isVisible();
        assertThat(footer.locator("text=MySQL 8.0 (Port 3307)")).isVisible();

        // Verify removed labels are completely absent
        assertThat(page.locator("text=Design Foundation & Curated Discovery Prototype (Phase 1)")).hasCount(0);
        assertThat(page.locator("text=Emil Kowalski Design Tokens")).hasCount(0);
        assertThat(footer.locator("text=WCAG 2.2 AA")).hasCount(0);
    }

    @Test
    @DisplayName("Verifies responsive layout of footer on mobile viewport without horizontal overflow")
    void testFooterResponsiveOnMobileViewport() {
        page = createMobilePage();
        page.navigate("/");

        Locator footer = page.locator("footer");
        assertThat(footer).isVisible();
        assertThat(footer.locator("text=Voyage-AI")).isVisible();
        assertThat(footer.locator("text=Spring Boot 4.1.1 (Port 8081)")).isVisible();
        assertThat(footer.locator("text=MySQL 8.0 (Port 3307)")).isVisible();

        // Confirm removed text is absent
        assertThat(page.locator("text=Design Foundation & Curated Discovery Prototype (Phase 1)")).hasCount(0);
        assertThat(page.locator("text=Emil Kowalski Design Tokens")).hasCount(0);

        // Verify no horizontal overflow on mobile
        Boolean hasHorizontalOverflow = (Boolean) page.evaluate("() => {" +
                "  const clientWidth = document.documentElement.clientWidth || window.innerWidth;" +
                "  return document.documentElement.scrollWidth > clientWidth + 2;" +
                "}");
        assertThat(hasHorizontalOverflow).isFalse();
    }
}
