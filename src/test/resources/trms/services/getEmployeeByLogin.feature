Feature: Using username and password to get employee

  Scenario: Correct username and password gets correct employee
    Given An employee exists
    When The employee's username and password are used
    Then The correct employee is retrieved

  Scenario: Incorrect username and correct password does not get employee
    Given An employee exists
    When An incorrect username and correct password are used
    Then No employee is retrieved

  Scenario: Correct username and incorrect password does not get employee
    Given An employee exists
    When A correct username and incorrect password are used
    Then No employee is retrieved

  Scenario: Username and password for a deleted employee does not get employee
    Given An employee exists
    And The employee was deleted
    When The employee's username and password are used
    Then No employee is retrieved

