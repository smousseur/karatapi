Feature: Simple tests for User API
  Background:
    * url 'http://localhost:8080/example'

  Scenario: Get a user
    Given path 'user'
    And param id = 51
    When method get
    Then status 200