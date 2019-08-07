@user
Feature: Social Network user feature testing
# Scenarios might fail because sometimes the server returns 503 after trying to get response for a long time

  Scenario Outline: Get existing user
    When I make a request to get user with <id>
    Then user status code is 200
    And user returned should have valid data

    Examples:
      | id |
      | 1  |
      | 10 |

  Scenario Outline: Get not existing user
    When I make a request to get user with <id>
    Then user status code is 404
    And user response should be empty

    Examples:
      | id   |
      | 0    |
      | 11   |
      | 5000 |

  # Scenario is failing due to mock api not actually posting the newly created user
  # User post request returns 201 success but when the script performs the get request by userId for the newly created user the api returns 404 not found
  Scenario: Create a new user
    Given I have valid user data
    When I make a request to create user on the social network
    Then user status code is 201
    When I make a request to get created user
    Then user status code is 200
    And correct user should be returned
    When I make a request to get users
    Then user status code is 200
    And new user should be added
