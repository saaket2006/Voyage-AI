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

@DisplayName("Destination Detail Modal and Accessibility Tests")
public class DestinationDetailModalTest extends BasePlaywrightTest {

    @BeforeEach
    void navigateToDiscovery() {
        page.navigate("/");
    }

    @Test
    @DisplayName("Opens modal when a destination card explore button is clicked")
    void testOpensModalOnExploreButtonClick() {
        // Click Explore button for Jaipur
        Locator exploreBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Explore details for Jaipur"));
        exploreBtn.click();

        // Dialog should appear
        Locator dialog = page.getByRole(AriaRole.DIALOG);
        assertThat(dialog).isVisible();

        // Verify modal title
        assertThat(dialog.getByRole(AriaRole.HEADING, new Locator.GetByRoleOptions().setLevel(2).setName("Jaipur"))).isVisible();
        assertThat(dialog.locator("text=Rajasthan, India")).isVisible();

        // Verify weather info is displayed
        assertThat(dialog.locator("text=°C")).isVisible();
    }

    @Test
    @DisplayName("Switches tabs inside the modal to inspect stays, dining, attractions, and budget")
    void testSwitchesTabsInsideModal() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Explore details for Jaipur")).click();
        Locator dialog = page.getByRole(AriaRole.DIALOG);
        assertThat(dialog).isVisible();

        // Click 'Stays' tab
        Locator staysTab = dialog.getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName(Pattern.compile("stays", Pattern.CASE_INSENSITIVE)));
        staysTab.click();
        assertThat(staysTab).hasAttribute("aria-selected", "true");
        assertThat(dialog.locator("text=Hand-vetted boutique havelis")).isVisible();

        // Click 'Dining' tab
        Locator diningTab = dialog.getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName(Pattern.compile("dining", Pattern.CASE_INSENSITIVE)));
        diningTab.click();
        assertThat(diningTab).hasAttribute("aria-selected", "true");
        assertThat(dialog.locator("text=Signature local dining")).isVisible();

        // Click 'Attractions' tab
        Locator attractionsTab = dialog.getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName(Pattern.compile("attractions", Pattern.CASE_INSENSITIVE)));
        attractionsTab.click();
        assertThat(attractionsTab).hasAttribute("aria-selected", "true");
        assertThat(dialog.locator("text=Est. Entry Fee").first()).isVisible();

        // Click 'Budget' tab
        Locator budgetTab = dialog.getByRole(AriaRole.TAB, new Locator.GetByRoleOptions().setName(Pattern.compile("budget", Pattern.CASE_INSENSITIVE)));
        budgetTab.click();
        assertThat(budgetTab).hasAttribute("aria-selected", "true");
        assertThat(dialog.locator("text=Honest Data Provenance & Illustrative Estimates")).isVisible();
        assertThat(dialog.locator("text=Budget Explorer")).isVisible();
        assertThat(dialog.getByText("Comfort Curated (Recommended)")).isVisible();
    }

    @Test
    @DisplayName("Closes modal via close button and restores focus")
    void testClosesModalViaCloseButton() {
        Locator exploreBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Explore details for Jaipur"));
        exploreBtn.click();

        Locator dialog = page.getByRole(AriaRole.DIALOG);
        assertThat(dialog).isVisible();

        // Click Close Button
        Locator closeBtn = dialog.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Close modal"));
        closeBtn.click(new Locator.ClickOptions().setForce(true));

        // Modal should be dismissed
        assertThat(dialog).not().isVisible();
    }

    @Test
    @DisplayName("Dismisses modal when Escape key is pressed")
    void testDismissesModalWhenEscapeKeyPressed() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Explore details for Jaipur")).click();

        Locator dialog = page.getByRole(AriaRole.DIALOG);
        assertThat(dialog).isVisible();

        // Press Escape
        page.keyboard().press("Escape");

        // Dialog should close
        assertThat(dialog).not().isVisible();
    }
}
