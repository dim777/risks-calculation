package ru.techlab.risks.calculation.acceptance;

import cucumber.api.PendingException;
import cucumber.api.Transpose;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.model.SimpleLoan;
import ru.techlab.risks.calculation.repository.LoansRepository;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.xegex.risks.libs.ex.delays.NoDelaysFoundEx;

import java.util.List;

/**
 * Created by rb052775 on 28.08.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoansDefs {
    @Autowired
    private DelayService delayService;

    /*@Given("^user pass datetime range for test \\(from: \"([^\"]*)\" till: \"([^\"]*)\"\\)$")
    public void aUserHasPostedTheFollowingDateTimeRange(String from, String till) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }*/

    @Given("^account number with no delays$")
    public void account_number_with_no_delays(@Transpose List<SimpleLoan> loans) throws NoDelaysFoundEx {
        for(loan : loans){
            delayService.getDelaysByLoan(loan);
        }
    }

    @Given("^input number account for request$")
    public void input_number_account_for_request(@Transpose List<SimpleLoan> loans){

    }

    @When("^the client calls1 /version$")
    public void the_client_issues_GET_version() throws Throwable{
        System.out.print("ddd");
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {


    }

    @And("^the client receives server version (.+)$")
    public void the_client_receives_server_version_body(String version) throws Throwable {

    }

}
