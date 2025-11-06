# Report Portal -  Automation Framework

Automated testing framework built in **Java 17** using **Playwright**, **JUnit 5**, and **Allure Reports**  
to validate UI flows of **EPAM Report Portal**.

## Tech Stack

- **Java 17**
- **Playwright** (UI automation)
- **JUnit 5** (testing framework)
- **Allure Reports** (test reporting)
- **AssertJ** (assertions)
- **SLF4J + Logback** (logging)
- **Maven** (build & dependency management)


## Setup & Execution

### 1. Configure environment variables

- export EPAM_EMAIL="your_email@epam.com"
- export EPAM_PASSWORD="your_password"


### 2. Run the test

- mvn clean verify -Denv=qa

### 3. Check Allure Report

- /target/site/allure-maven-plugin


