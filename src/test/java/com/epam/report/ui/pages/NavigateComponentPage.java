package com.epam.report.ui.pages;


import com.microsoft.playwright.Page;


public class NavigateComponentPage {

    private final Page page;

    public NavigateComponentPage(Page page) {
        this.page = page;
    }


    public int getListOptions() {
        page.waitForSelector("[class*='sidebar__top-block']",
                new Page.WaitForSelectorOptions().setTimeout(15000)
        );
        return page.locator("[class*='sidebar__top-block'] [class*='sidebar__sidebar-btn']").count();
    }
}
