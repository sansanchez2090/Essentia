Feature: Perfume Management
  As a client of the Essentia API
  I want to manage perfumes
  So that the system can store and update perfume information

  Scenario: Get all perfumes
    When I send a GET request to "/perfumes"
    Then the response status should be 200
    And the response should contain a list of perfumes

  Scenario: Create a new perfume
    Given I have a valid perfume payload
    When I send a POST request to "/perfumes"
    Then the response status should be 201
    And the response should contain the created perfume

  Scenario: Update an existing perfume
    Given a perfume exists with ID 1
    And I have an updated perfume payload
    When I send a PUT request to "/perfumes/1"
    Then the response status should be 200
    And the response should contain the updated perfume

  Scenario: Delete a perfume
    Given a perfume exists with ID 1
    When I send a DELETE request to "/perfumes/1"
    Then the response status should be 204
