
Feature: Get Remaining Reimbursement
  # Will execute these steps before each scenario
  Background:
    Given An employee exists
    #
  Scenario Outline: Remaining reimbursement for an employee is retrieved
    Given The employee has submitted <recent requests> requests for <recent amount> dollars each recently
    And The employee has submitted <stale number> requests for <stale amount> dollars each <days> days ago
    And The most recent request was denied or not? It was "<denied or not>"
    When Remaining reimbursement is retrieved
    Then Remaining reimbursement is <remaining reimbursement> dollars
    #
  Examples:
    | recent requests | recent amount | stale number | stale amount | days | denied or not | remaining reimbursement |
    | 0               | 0             | 0            | 0            | 0    | not denied    | 1000                    |
    | 1               | 400           | 0            | 0            | 0    | not denied    | 600                     |
    | 2               | 400           | 0            | 0            | 0    | not denied    | 200                     |
    | 3               | 400           | 0            | 0            | 0    | not denied    | 0                       |
    | 1               | 2000          | 0            | 0            | 0    | not denied    | 0                       |
    | 0               | 0             | 1            | 500          | 400  | not denied    | 1000                    |
    | 1               | 800           | 0            | 0            | 0    | denied        | 1000                    |
    | 2               | 800           | 0            | 0            | 0    | denied        | 200                     |


