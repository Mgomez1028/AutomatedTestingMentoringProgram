package com.epam.report.utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FakerUtils {

    private static final Faker faker = new Faker(new Locale("en"));

    public static String randonDashboardName() {
        String name = faker.app().name().replaceAll("[^a-zA-Z0-9]", "");
        return truncate(name, 20);
    }

    public static String randonDescription() {
        String description = faker.lorem().sentence(3).replaceAll("[^a-zA-Z0-9]", "");
        return truncate(description, 20);
    }

    public static String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength) : text;
    }
}
