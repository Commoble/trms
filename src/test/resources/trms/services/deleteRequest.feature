Feature: Delete request for employee
  Scenario: Employee deletes their own request
    Given An employee exists
    And Employee has a pending request
    When Employee deletes request
    Then Request has been deleted
    And Request does not exist
    #

  Scenario: Employee deletes a request owned by another employee
    Given An employee exists
    And Employee has a pending request
    And A second employee exists
    When Second employee deletes first employee's request
    Then Request is not deleted
    And Request still exists
    #

  Scenario: Employee deletes a request that doesn't exist
    Given An employee exists
    And Employee has a pending request
    When Employee deletes request
    And Employee deletes request
    Then Request is not deleted
    And Request does not exist