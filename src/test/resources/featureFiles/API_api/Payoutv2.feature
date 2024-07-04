Feature:  Payout v2


  @API
  Scenario: Get Balance
    When Verify get balance

  @API
  Scenario: IMPS Payout
    When Verify decimal value request
    When Verify VPA is required
    When Verify bank acc number is required
    When Verify IMPS payout required fields
    When Verify Order Id already Exists
    When Send IMPS payout request
    When Make Transaction Fail Success
    Then Verify Transaction status

  @API
  Scenario: UPI Payout
    When  Send UPI payout request
    When Make Transaction Fail Success
    Then Verify Transaction status

  @API
  Scenario: Get Status
    When Verify transaction not found
    When Verify required fields get status
