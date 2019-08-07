@comment
Feature: Social Network comment feature testing

  Scenario Outline: Get existing comment
    When I make a request to get comment with <id>
    Then comment status code is 200
    And comment returned should have valid data

    Examples:
      | id  |
      | 10  |
      | 500 |

  Scenario Outline: Get not existing comment
    When I make a request to get comment with <id>
    Then comment status code is 404
    And comment response should be empty

    Examples:
      | id   |
      | 0    |
      | 501  |
      | 5000 |

  # Scenario is failing due to mock api not actually posting the new comment
  # Comment post request returns 201 success but when the script performs the get request for all post comments the new comment is not found in the list returned
  Scenario Outline: Make a new comment on valid post
    Given I want to write a comment with <postId>, <name>, <email> and <content>
    When I make a request to post comment on the social network
    Then comment status code is 201
    When I make a request to get post comments by postId
    Then comment status code is 200
    And new comment should be added

    Examples:
      | postId | name                                    | email               | content                              |
      | 1      | This is a test comment                  | automation@test.com | This is a valid comment scenario     |
      | 50     | This is a special characters comment :) | automation@test.com | `1234567890-=~!@#$%^&*()_+[]\",./<>? |

  # Scenario is failing due to mock api not actually validating if the post exists
  # Comment post request returns 201 success even thought the post we are trying to comment on does not exist
  Scenario Outline: Make a new comment on invalid post
    Given I want to write a comment with <postId>, <name>, <email> and <content>
    When I make a request to post comment on the social network
    Then comment status code is 400
    When I make a request to get post comments by postId
    Then comment status code is 200
    And new comment should not be added

    Examples:
      | postId | name                   | email               | content                                        |
      | 0      | This is a test comment | automation@test.com | This is a invalid postId comment scenario      |
      | 101    | This is a test comment | automation@test.com | This is a not existing postId comment scenario |

  # Scenario is failing due to mock api not actually validating the comment data
  # Comment post request returns 201 success even thought the comment data is invalid
  Scenario Outline: Make an invalid comment
    Given I want to write a comment with <postId>, <name>, <email> and <content>
    When I make a request to post comment on the social network
    Then comment status code is 400
    When I make a request to get post comments by postId
    Then comment status code is 200
    And new comment should not be added

    Examples:
      | postId | name                             | email               | content                                   |
      | 1      |                                  | automation@test.com | This is an empty name value comment test  |
      | 10     | This is a test comment           |                     | This is an empty email value comment test |
      | 50     | This is an empty content comment | automation@test.com |                                           |
