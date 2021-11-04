Feature: Reimbursement request added for employee

  Scenario: Add request for employee adds request for employee
    Given An employee exists
    And Employee desires to request a new reimbursement request
    When Request added for employee
    Then The request response is correct
    #

  Scenario: Add request for missing employee doesn't add request
    Given An employee exists
    And The employee was deleted
    And Employee desires to request a new reimbursement request
    When Request added for employee
    Then The request response status is not found
    #

  Scenario: Add request for missing event type doesn't add request
    Given An employee exists
    And Employee desires to request a new reimbursement request
    And Request has bad event type
    When Request added for employee
    Then The request response status is unprocessable entity
    #

  Scenario: Add request for invalid event cost doesn't add request
    Given An employee exists
    And Employee desires to request a new reimbursement request
    And Request has bad event cost
    When Request added for employee
    Then The request response status is unprocessable entity
    #

  Scenario: Add request for unfundable event doesn't add request
    Given An employee exists
    And Employee desires to request a new reimbursement request
    And The employee has no remaining reimbursements due to pending requests
    When Request added for employee
    Then The request response status is unprocessable entity
    #

  Scenario: Add request for invalid grading format doesn't add request
    Given An employee exists
    And Employee desires to request a new reimbursement request
    And Request has bad grading format
    When Request added for employee
    Then The request response status is unprocessable entity