Feature: Simple tests for Order API
  Background:
    * url 'http://' + karate.properties['appHost'] + ":" + karate.properties['appPort'] + '/example'

  Scenario: Get a user
    Given path 'order/list'
    When method get
    Then status 200