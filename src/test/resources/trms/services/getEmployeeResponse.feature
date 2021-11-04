Feature: Using employee id to get employee response

  Scenario: Logged in employee gets their own employee data
    Given An employee exists
    When The employee uses their own id to get employee response
    Then The employee response is retrieved

  Scenario: Logged in employee gets a different employee's data
    Given An employee exists
    And A second employee exists
    When The employee uses the other employee's id to get employee response
    Then No employee response is retrieved
