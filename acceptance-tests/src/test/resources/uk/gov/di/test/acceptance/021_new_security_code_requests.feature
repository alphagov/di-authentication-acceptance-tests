Feature: New security code requests
  User requests new security code until they get warned they have reached the limit

  Scenario: New user requests new security code too many times
    Given the registration services are running
    And a new user has valid credentials
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user requests a new security code 5 times
    Then the new user is taken to the you cannot get a new security code at the moment page
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is taken to the you requested too many security codes page

