package uk.gov.di.test.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginStepDefinitions extends SignInStepDefinitions {

    private String emailAddress;
    private String password;

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @After
    public void closeWebdriver() {
        super.closeWebdriver();
    }

    @Given("the login services are running")
    public void theServicesAreRunning() {}

    @And("the existing user has valid credentials")
    public void theExistingUserHasValidCredentials() {
        emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        password = "password";
    }

    @And("the existing user has invalid credentials")
    public void theExistingUserHasInvalidCredentials() {
        emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        password = "wrong-password";
    }

    @When("the existing user visits the stub relying party")
    public void theExistingUserVisitsTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the existing user clicks {string}")
    public void theExistingUserClicks(String buttonName) {
        WebElement button = driver.findElement(By.id(buttonName));
        button.click();
    }

    @Then("the existing user is taken to the Identity Provider Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoad("Sign in or create a GOV.UK account");
        assertEquals("/sign-in-or-create", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(IDP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals("Sign in or create a GOV.UK account - GOV.UK Account", driver.getTitle());
    }

    @When("the existing user enters their email address")
    public void theExistingUserEntersEmailAddress() {
        WebElement emailAddressField = driver.findElement(By.id("email"));
        emailAddressField.sendKeys(emailAddress);
        WebElement continueButton =
                driver.findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    @Then("the existing user is prompted for password")
    public void theExistingUserIsPromptedForPassword() {
        assertEquals("/login", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Sign-in to GOV.UK - Password", driver.getTitle());
    }

    @When("the existing user enters their password")
    public void theExistingUserEntersTheirPassword() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
    }

    @Then("the existing user is taken to the Service User Info page")
    public void theExistingUserIsTakenToTheServiceUserInfoPage() {
        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(RP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals(RP_URL.getPort(), URI.create(driver.getCurrentUrl()).getPort());
        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
        WebElement emailDescriptionDetails = driver.findElement(By.id("user-info-email"));
        assertEquals(emailAddress, emailDescriptionDetails.getText().trim());
    }

    @Then("the existing user is shown an error message")
    public void theExistingUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        WebElement emailDescriptionDetails = driver.findElement(By.id("error-summary-title"));
        assertTrue(emailDescriptionDetails.isDisplayed());
    }
}