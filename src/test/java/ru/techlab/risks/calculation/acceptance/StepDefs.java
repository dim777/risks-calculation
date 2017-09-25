package ru.techlab.risks.calculation.acceptance;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by rb052775 on 28.08.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StepDefs{
    @Given("^user pass datetime range for test \\(from: \"([^\"]*)\" till: \"([^\"]*)\"\\)$")
    public void aUserHasPostedTheFollowingDateTimeRange(String from, String till) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
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
