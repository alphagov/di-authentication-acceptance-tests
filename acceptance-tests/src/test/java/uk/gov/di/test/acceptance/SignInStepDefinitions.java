package uk.gov.di.test.acceptance;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignInStepDefinitions {
    protected static final String CHROME_BROWSER = "chrome";
    protected static final String FIREFOX_BROWSER = "firefox";
    protected static final String SELENIUM_URL = System.getenv().get("SELENIUM_URL");
    protected static final String IDP_URL =
            System.getenv().getOrDefault("IDP_URL", "http://localhost:8080/");
    protected static final String RP_URL =
            System.getenv().getOrDefault("RP_URL", "http://localhost:8081/");
    protected static final String AM_URL =
            System.getenv().getOrDefault("AM_URL", "http://localhost:8081/");
    protected static final Boolean SELENIUM_LOCAL =
            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_LOCAL", "false"));
    protected static final Boolean SELENIUM_HEADLESS =
            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_HEADLESS", "true"));
    protected static final Boolean DEBUG_MODE =
            Boolean.parseBoolean(System.getenv().getOrDefault("DEBUG_MODE", "false"));
    protected static final String SELENIUM_BROWSER =
            System.getenv().getOrDefault("SELENIUM_BROWSER", FIREFOX_BROWSER);
    protected static final Duration DEFAULT_PAGE_LOAD_WAIT_TIME = Duration.of(20, SECONDS);
    protected static WebDriver driver;

    protected void setupWebdriver() throws MalformedURLException {
        if (driver == null) {
            switch (SELENIUM_BROWSER) {
                case CHROME_BROWSER:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setHeadless(SELENIUM_HEADLESS);
                    if (SELENIUM_LOCAL) {
                        driver = new ChromeDriver(chromeOptions);
                    } else {
                        driver = new RemoteWebDriver(new URL(SELENIUM_URL), chromeOptions);
                    }
                    break;
                default:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setHeadless(SELENIUM_HEADLESS);
                    firefoxOptions.setPageLoadTimeout(Duration.of(30, SECONDS));
                    firefoxOptions.setImplicitWaitTimeout(Duration.of(30, SECONDS));
                    if (SELENIUM_LOCAL) {
                        driver = new FirefoxDriver(firefoxOptions);
                    } else {
                        driver = new RemoteWebDriver(new URL(SELENIUM_URL), firefoxOptions);
                    }
            }
        }
    }

    protected void closeWebdriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected void waitForPageLoad(String titleContains) {
        new WebDriverWait(driver, DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.titleContains(titleContains));
    }

    protected void waitForPageLoadThenValidate(AuthenticationJourneyPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(page.getFullTitle(), driver.getTitle());
    }

    protected void findAndClickContinue() {
        WebElement continueButton =
                driver.findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    protected void findAndClickButtonByText(String buttonText) {
        WebElement continueButton =
                driver.findElement(
                        By.xpath("//button[text()[normalize-space() = '" + buttonText + "']]"));
        continueButton.click();
    }
}
