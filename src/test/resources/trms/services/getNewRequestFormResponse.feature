Feature: Get new request form response

  Scenario: Employee gets new request form response
    Given An employee exists
    When Employee gets new request form response
    Then New request form response has one thousand dollars remaining