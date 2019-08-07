@post
Feature: Social Network post feature testing

  Scenario Outline: Get existing post
    When I make a request to get post with <id>
    Then post status code is 200
    And post returned should have valid data

    Examples:
      | id |
      | 10 |
      | 1  |

  Scenario Outline: Get not existing post
    When I make a request to get post with <id>
    Then post status code is 404
    And post response should be empty

    Examples:
      | id   |
      | 0    |
      | 101  |
      | 5000 |

  # Scenario is failing due to mock api not actually posting the new post
  # Post post request returns 201 success but when the script performs the get request by postId for the newly created post the api returns 404 not found
  Scenario Outline: Valid user makes a new post
    Given I want to write a post with <userID>, <title> and <content>
    When I make a request to post on the social network
    Then post status code is 201
    When I make a request to get post
    Then post status code is 200
    And correct post should be returned

    Examples:
      | userID | title                                     | content                              |
      | 10     | This is a test post                       | Test for posting                     |
      | 1      | This is a special characters test post :) | `1234567890-=~!@#$%^&*()_+[]\",./<>? |

  # Scenario is failing due to mock api not actually validating if the user exists
  # Post post request returns 201 success even thought the user trying to post does not exist
  Scenario Outline: Invalid user makes a new post
    Given I want to write a post with <userID>, <title> and <content>
    When I make a request to post on the social network
    Then post status code is 400
    When I make a request to get all posts
    Then post status code is 200
    And post should not be found in list

    Examples:
      | userID | title               | content                         |
      | 5000   | This is a test post | Test post for not existing user |
      | 0      | This is a test post | Test post for invalid user      |
      | 11     | This is a test post | Test post for not existing user |

  # Scenario is failing due to mock api not actually validating the post data
  # Post post request returns 201 success even thought the post data is invalid
  Scenario Outline: User makes an invalid post
    Given I want to write a post with <userID>, <title> and <content>
    When I make a request to post on the social network
    Then post status code is 400
    When I make a request to get all posts
    Then post status code is 200
    And post should not be found in list

    Examples:
      | userID | title                                 | content                    |
      | 10     |                                       | Test empty title test post |
      | 1      | This is a test for empty content post |                            |

  Scenario Outline: User deletes one post
    When I delete post by <id>
    Then post status code is 200
    And post response should be empty

    Examples:
      | id |
      | 10 |
      | 1  |