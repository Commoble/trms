Feature: Get requests approvable by getting employee

  Scenario: Request is not yet approved
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that does not require a presentation
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is in the approvable requests
    When Department head gets approvable requests
    Then The request is not in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is not in the approvable requests

  Scenario: Supervisor has approved request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that does not require a presentation
    And Supervisor has approved request
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is not in the approvable requests
    When Department head gets approvable requests
    Then The request is in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is not in the approvable requests

  Scenario: Department head has approved request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that does not require a presentation
    And Supervisor has approved request
    And Department head has approved request
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is not in the approvable requests
    When Department head gets approvable requests
    Then The request is not in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is in the approvable requests

  Scenario: Benefits coordinator has approved request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that does not require a presentation
    And Supervisor has approved request
    And Department head has approved request
    And Benefits coordinator has approved request
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is not in the approvable requests
    When Department head gets approvable requests
    Then The request is not in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is in the approvable requests

  Scenario: Benefits coordinator has awarded request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that does not require a presentation
    And Supervisor has approved request
    And Department head has approved request
    And Benefits coordinator has approved request
    And Benefits coordinator has awarded request
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is not in the approvable requests
    When Department head gets approvable requests
    Then The request is not in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is not in the approvable requests

  Scenario: Benefits coordinator has approved request for presentation format
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that requires a presentation
    And Supervisor has approved request
    And Department head has approved request
    And Benefits coordinator has approved request
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is in the approvable requests
    When Department head gets approvable requests
    Then The request is not in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is not in the approvable requests

  Scenario: Supervisor has awarded request
    Given An employee exists
    And A supervisor exists
    And A department head exists
    And A benefits coordinator exists
    And A request for employee exists that requires a presentation
    And Supervisor has approved request
    And Department head has approved request
    And Benefits coordinator has approved request
    And Supervisor has awarded request
    When Employee gets approvable requests
    Then The request is not in the approvable requests
    When Supervisor gets approvable requests
    Then The request is not in the approvable requests
    When Department head gets approvable requests
    Then The request is not in the approvable requests
    When Benefits coordinator gets approvable requests
    Then The request is not in the approvable requests
    #

  Scenario: Invalid employee gets approvable requests
    Given An employee exists
    And The employee was deleted
    When Employee gets approvable requests
    Then The request is not in the approvable requests