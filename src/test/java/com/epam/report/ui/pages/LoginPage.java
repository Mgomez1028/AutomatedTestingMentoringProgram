package com.epam.report.ui.pages;

import com.epam.report.utils.ConfigReader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {

    private final Page page;
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage(Page page) {
        this.page = page;
    }

    @Step("Open Report Portal Page")
    public void navigateToReportPage() {
        String baseUrl = ConfigReader.get("base.url");
        String loginPath = ConfigReader.get("login.path");
        String loginUrl = baseUrl + loginPath;
        page.navigate(baseUrl + loginPath);

        log.info("Login to Report Portal home: {}", loginUrl);
    }

    private void shouldLoginWithUserAndPassword() {
        String password = System.getenv("EPAM_PASSWORD");
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("Env. variable EPAM_PASSWORD is not defined");
        }

        page.locator("input[name='passwd']").fill(password);
        page.locator("#idSIButton9").click();
        page.waitForLoadState();
    }

    private void shouldLoginWithMFA() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    private void startLogin() {
        String email = System.getenv("EPAM_EMAIL");
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Env. variable EPAM_EMAIL is not defined");
        }
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login with EPAM")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("FirstName_LastName@DomainName")).fill(email);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
    }

    @Step("Starting Login process")
    public void defineLoginMethod() {
        startLogin();

        page.waitForTimeout(3000);

        Locator passwordInput = page.locator("input[name='passwd']");
        Locator mfaContainer = page.locator("div.display-sign-container");

        try {
            if (passwordInput.isVisible(new Locator.IsVisibleOptions().setTimeout(3000))) {
                log.info("Password screen detected");
                shouldLoginWithUserAndPassword();
                return;
            }
        } catch (Exception ignored) {
        }

        try {
            if (mfaContainer.isVisible(new Locator.IsVisibleOptions().setTimeout(3000))) {
                log.info("MFA screen detected");
                shouldLoginWithMFA();
                return;
            }
        } catch (Exception ignored) {
        }

        log.warn("No known login screen was detected");

        //validate url to work on other functions
        page.waitForURL("https://reportportal.epam.com/ui/#martha_gomez_personal/dashboard", new Page.WaitForURLOptions().setTimeout(15000));
        System.out.println("page tittle " + page.title());
        Assertions.assertThat(page.title())
                .as("validating success login")
                .contains("Report Portal");
    }

    public String getTitle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return page.title();
    }
}
