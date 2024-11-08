Feature: Simple tests for Order API
  Background:
    * url 'http://localhost:8080/example'

  Scenario: Get a user
    Given path 'order'
    And param orderId = 51
    When method get
    Then status 200