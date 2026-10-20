package com.voyageai.tests.base;

import com.microsoft.playwright.*;
import com.voyageai.tests.config.TestConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Base test class for Java Playwright test suites.
 * Handles Playwright lifecycle, browser contexts, viewport setup, console error tracking,
 * and automatic screenshot/trace capture upon test failure.
 */
public abstract class BasePlaywrightTest {

    protected static Playwright playwright;
    protected static Browser browser;

    protected BrowserContext context;
    protected Page page;
    protected List<String> consoleErrors = new ArrayList<>();
    private boolean testFailed = false;
    private String currentTestName = "test";

    @RegisterExtension
    final TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext extContext, Throwable cause) {
            testFailed = true;
        }
    };

    @BeforeAll
    public static void beforeAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.isHeadless()));
    }

    @AfterAll
    public static void afterAll() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        testFailed = false;
        consoleErrors.clear();
        currentTestName = testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "_");

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 800)
                .setBaseURL(TestConfig.getFrontendBaseUrl()));

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.setDefaultTimeout(TestConfig.getTimeoutMs());

        page.onConsoleMessage(msg -> {
            if ("error".equalsIgnoreCase(msg.type())) {
                consoleErrors.add(msg.text());
            }
        });
    }

    @AfterEach
    public void tearDown() {
        try {
            if (testFailed && context != null) {
                Path traceDir = Paths.get("target", "playwright-traces");
                Path screenshotDir = Paths.get("target", "playwright-screenshots");
                Files.createDirectories(traceDir);
                Files.createDirectories(screenshotDir);

                if (page != null) {
                    page.screenshot(new Page.ScreenshotOptions()
                            .setPath(screenshotDir.resolve(currentTestName + ".png")));
                }
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(traceDir.resolve(currentTestName + ".zip")));
            } else if (context != null) {
                context.tracing().stop();
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to save test failure artifacts: " + e.getMessage());
        } finally {
            if (page != null) {
                page.close();
                page = null;
            }
            if (context != null) {
                context.close();
                context = null;
            }
        }
    }

    /**
     * Creates a mobile browser context (Pixel 5 viewport: 393x851)
     */
    protected Page createMobilePage() {
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(393, 851)
                .setUserAgent("Mozilla/5.0 (Linux; Android 13; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                .setIsMobile(true)
                .setHasTouch(true)
                .setBaseURL(TestConfig.getFrontendBaseUrl()));

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.setDefaultTimeout(TestConfig.getTimeoutMs());

        page.onConsoleMessage(msg -> {
            if ("error".equalsIgnoreCase(msg.type())) {
                consoleErrors.add(msg.text());
            }
        });

        return page;
    }

    /**
     * Reads a test fixture string from tests/fixtures or src/test/resources
     */
    protected String readFixture(String relativePath) {
        try {
            // First try relative path from tests/ directory
            Path filePath = Paths.get(relativePath);
            if (Files.exists(filePath)) {
                return Files.readString(filePath);
            }
            // Next try from root directory
            Path rootPath = Paths.get("tests", relativePath);
            if (Files.exists(rootPath)) {
                return Files.readString(rootPath);
            }
            // Finally try classpath
            try (var is = getClass().getClassLoader().getResourceAsStream(relativePath)) {
                if (is != null) {
                    return new String(is.readAllBytes());
                }
            }
            throw new RuntimeException("Test fixture not found: " + relativePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading fixture: " + relativePath, e);
        }
    }
}
