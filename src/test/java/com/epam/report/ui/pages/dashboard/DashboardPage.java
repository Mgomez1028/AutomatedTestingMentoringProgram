package com.epam.report.ui.pages.dashboard;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;

import java.util.List;
import java.util.regex.Pattern;


public class DashboardPage {

    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    @Step("Create a new Dashboard")
    public void createDashboard(String name, String description) {
        page.getByRole(AriaRole.LINK).filter(new Locator.FilterOptions().setHasText("Dashboards")).click();
        page.getByRole(AriaRole.LINK).filter(new Locator.FilterOptions().setHasText("Dashboards")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add New Dashboard")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter dashboard name")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter dashboard name")).fill(name);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter dashboard description")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter dashboard description")).fill(description);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add").setExact(true)).click();
        page.getByRole(AriaRole.LINK).filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Dashboards$"))).click();
    }

    @Step("Get Dashboard list")
    public List<DashboardItem> getDashboardItems() {
        Locator rows = page.locator("div[class*='dashboardTable__dashboard'] div[class*='gridRow__grid-row-wrapper']");
        page.waitForCondition(() -> rows.count() > 0, new Page.WaitForConditionOptions().setTimeout(5000));

        return rows
                .all()
                .stream()
                .map(
                        row -> {
                            String name = row.locator("[class*='dashboardTable__name']").innerText().trim();
                            String description = row.locator("[class*='dashboardTable__description']").innerText().trim();
                            return new DashboardItem(name, description);
                        }
                ).toList();
    }


    @Step("Get a specific Dashboard")
    public String getDashboardItem(String prjName) {
        Locator dashboardName = page.locator("div[class*='gridRow__grid-row'] a", new Page.LocatorOptions().setHasText(prjName));
        dashboardName.waitFor(new Locator.WaitForOptions().setTimeout(3000));
        return dashboardName.innerText();

    }

    @Step("Search Dashboard by name")
    public void searchByName(String dashboardName) {
        page.getByRole(AriaRole.LINK).filter(new Locator.FilterOptions().setHasText("Dashboards")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search by name")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search by name")).fill(dashboardName);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search by name")).press("Enter");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Step("Delete Dashboard")
    public void deleteDashboard(String dashboardName) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(dashboardName)).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
        page.locator("#modal-root").getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Delete")).click();
        page.locator(".grid__grid--W4yQA").click();
    }
}
