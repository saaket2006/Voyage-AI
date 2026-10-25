package com.voyageai.tests.browser;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.voyageai.tests.base.BasePlaywrightTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Responsive Layout and Viewport Tests")
public class ResponsiveTest extends BasePlaywrightTest {

    @Test
    @DisplayName("Renders cleanly on mobile viewport (393x851) without horizontal scrollbar overflow")
    void testRendersCleanlyOnMobileViewport() {
        page = createMobilePage();
        page.navigate("/");

        // Brand logo and title should be visible
        assertThat(page.locator("text=Voyage-AI").first()).isVisible();

        // Verify search input is accessible on mobile
        Locator searchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(Pattern.compile("search", Pattern.CASE_INSENSITIVE)));
        assertThat(searchInput).isVisible();

        // Verify filter pills wrap cleanly and North India button is clickable
        Locator northBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("North India").setExact(true));
        assertThat(northBtn).isVisible();
        northBtn.click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Jaipur"))).isVisible();

        // Check no horizontal scrollbar overflow
        Boolean hasHorizontalOverflow = (Boolean) page.evaluate("() => {" +
                "  const clientWidth = document.documentElement.clientWidth || window.innerWidth;" +
                "  return document.documentElement.scrollWidth > clientWidth + 2;" +
                "}");
        assertThat(hasHorizontalOverflow).isFalse();
    }

    @Test
    @DisplayName("Renders multi-column grid layout on desktop viewport (1280x800) without overflow")
    void testRendersMultiColumnGridLayoutOnDesktop() {
        page.navigate("/");

        // Main navigation items visible on desktop
        Locator nav = page.locator("nav[aria-label=\"Main Navigation\"]");
        assertThat(nav).isVisible();

        // Multiple destination cards visible in the grid
        Locator cards = page.locator("article");
        assertThat(cards.first()).isVisible();
        int count = cards.count();
        assertThat(count).isGreaterThan(1);

        // Verify no horizontal overflow
        Boolean hasHorizontalOverflow = (Boolean) page.evaluate("() => {" +
                "  const clientWidth = document.documentElement.clientWidth || window.innerWidth;" +
                "  return document.documentElement.scrollWidth > clientWidth + 2;" +
                "}");
        assertThat(hasHorizontalOverflow).isFalse();
    }
}
