Feature: User Login

  Scenario: Login with valid credentials
    Given I have valid login credentials
    When I send a login request
    Then I should receive a successful response
