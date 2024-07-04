package StepDefs.Payoutv2;

import StepDefs.BaseStepDef;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Payoutv2TransactionStepDef extends BaseStepDef {


    @When("Verify get balance")
    public void verifyGetBalance() {
        payoutv2.getToken();
        payoutv2.getBalance();
    }
    @When("Verify VPA is required")
    public void verifyVPAIsRequired() {
        payoutv2.getToken();
        payoutv2.verifyVPArequired();
    }

    @When("Verify bank acc number is required")
    public void verifyBankAccNumberIsRequired() {
        payoutv2.verifyBankAccIfscRequired();
    }

    @When("Verify IMPS payout required fields")
    public void verifyIMPSPayoutRequiredFields() {
//        payoutv2.getToken();
        payoutv2.verifyImpsPayoutRequiredFields();
    }

    @When("Send IMPS payout request")
    public void sendIMPSPayoutRequest() {
        payoutv2.sendImpsPayoutRequest();
    }


    @When("Make Transaction Fail Success")
    public void makeTransactionFailSuccess() {
        payoutv2.makeTransactionSuccesFail();
    }

    @Then("Verify Transaction status")
    public void verifyTransactionStatus() {
        payoutv2.verifyTransactionStatus();
    }

    @When("Verify transaction not found")
    public void verifyTransactionNotFound() {
        payoutv2.getToken();
        payoutv2.txnNotFountGetStatus();
    }

    @When("Verify required fields get status")
    public void verifyRequiredFieldsGetStatus() {
        payoutv2.verifyInvalidMid();
    }

    @When("Send UPI payout request")
    public void sendUPIPayoutRequest() {
        payoutv2.getToken();
        payoutv2.sendUpiPayoutRequest();
    }

    @When("Verify decimal value request")
    public void verifyDecimalValueRequest() {
        payoutv2.getToken();
    payoutv2.verifyDecimalAmt();
    }
    @When("Verify Order Id already Exists")
    public void verifyOrderIdAlreadyExists() {
        payoutv2.verifyOrderIdAlreadyExists();
    }
}
