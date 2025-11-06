package com.epam.report.ui.test.dashboard;

import com.epam.report.ui.pages.NavigateComponentPage;
import com.epam.report.ui.fixtures.HeadlessChromeOptions;
import com.epam.report.ui.pages.LoginPage;
import com.epam.report.ui.pages.dashboard.DashboardItem;
import com.epam.report.ui.pages.dashboard.DashboardPage;

import com.epam.report.utils.FakerUtils;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;


@UsePlaywright(HeadlessChromeOptions.class)
public class DashboardTest {

    LoginPage loginPage;
    DashboardPage dashboardPage;
    NavigateComponentPage navigateComponentPage;

    @BeforeEach
    void setUp(Page page) {
        loginPage = new LoginPage(page);
        dashboardPage = new DashboardPage(page);
        navigateComponentPage = new NavigateComponentPage(page);

        loginPage.navigateToReportPage();
    }


    @Test
    void shouldNavigateToReportPage() {
        loginPage.defineLoginMethod();
        int options = navigateComponentPage.getListOptions();
        String title = loginPage.getTitle();
        System.out.println("options returned: " + options);

        Assertions.assertThat(title)
                .as("Report Portal Page loaded correctly")
                .contains("Report Portal");

        Assertions.assertThat(options)
                .as("Side menu loaded correctly")
                .isGreaterThan(1);
    }

    @DisplayName("Add Dashboard")
    @Test
    void shouldCreateNewDashboard() {
        String name = FakerUtils.randonDashboardName();
        String description = FakerUtils.randonDescription();

        loginPage.defineLoginMethod();
        dashboardPage.createDashboard(name, description);
        String dashboardName = dashboardPage.getDashboardItem(name);

        Assertions.assertThat(dashboardName)
                .as("Dashboard created correctly")
                .isEqualTo(name);
    }

    @DisplayName("Look for a Dashboard")
    @Test
    void shouldLookForAnElement() {
        String name = FakerUtils.randonDashboardName();
        String description = FakerUtils.randonDescription();

        loginPage.defineLoginMethod();
        dashboardPage.createDashboard(name, description);
        dashboardPage.searchByName(name);

        List<DashboardItem> dashboardItems = dashboardPage.getDashboardItems();

        Assertions.assertThat(dashboardItems)
                .as("Returns only one element and it is correct")
                .hasSize(1)
                .first()
                .satisfies(dashboardItem -> {
                    Assertions.assertThat(dashboardItem.name()).isEqualTo(name);
                    Assertions.assertThat(dashboardItem.description()).isEqualTo(description);
                });
    }

    @DisplayName("Delete Dashboard")
    @Test
    void shouldDeleteDashboard() {
        String name = FakerUtils.randonDashboardName();
        String description = FakerUtils.randonDescription();

        loginPage.defineLoginMethod();
        dashboardPage.createDashboard(name, description);
        dashboardPage.searchByName(name);
        dashboardPage.deleteDashboard(name);

        List<DashboardItem> dashboardItems = dashboardPage.getDashboardItems();
        System.out.println(dashboardItems);

        Assertions.assertThat(dashboardItems)
                .as("The item does not exist in the list after it has been deleted")
                .noneMatch(item -> item.name().equalsIgnoreCase(name));
    }

}

