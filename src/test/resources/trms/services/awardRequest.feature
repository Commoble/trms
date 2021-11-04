Feature: Employee awards request

  Scenario: Invalid employee awards request
    Given An employee exists
    And Employee has a pending request
    And The employee was deleted
    When Employee awards request
    Then Award response is not found
    #

  Scenario: Employee awards invalid request
    Given An employee exists
    And Employee has a pending request
    And The request was deleted
    When Employee awards request
    Then Award response is not found
    #

  Scenario: Benefits coordinator awards request when request is not approved
    Given An employee exists
    And A benefits coordinator exists
    And Employee has a pending request
    When Benefits coordinator awards request
    Then Award response is not found
    And Request has not been awarded
    #

  Scenario: Benefits coordinator awards request when only supervisor has approved
    Given An employee exists
    And A supervisor exists
    And A benefits coordinator exists
    And Employee has a pending request
    And Supervisor has approved request
    When Benefits coordinator awards request
    Then Award response is not found
    And Request has not been awarded
    #

  Scenario: Benefits coordinator awards request when only supervisor and department head have approved
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And Employee has a pending request
    And Supervisor has approved request
    And Department head has approved request
    When Benefits coordinator awards request
    Then Award response is not found
    And Request has not been awarded
    #

  Scenario: Benefits coordinator awards awardable request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that does not require a presentation
    And Supervisor has approved request
    And Department head has approved request
    And Benefits coordinator has approved request
    When Benefits coordinator awards request
    Then Award response is success
    And Request has been awarded
    #

  Scenario: Supervisor awards awardable presentation request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that requires a presentation
    And Supervisor has approved request
    And Department head has approved request
    And Benefits coordinator has approved request
    When Supervisor awards request
    Then Award response is success
    And Request has been awarded

