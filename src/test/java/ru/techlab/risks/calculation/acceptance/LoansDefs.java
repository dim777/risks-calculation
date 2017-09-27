package ru.techlab.risks.calculation.acceptance;

import cucumber.api.Transpose;
import cucumber.api.java.cs.A;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.techlab.risks.calculation.CassandraRepositoryTests;
import ru.techlab.risks.calculation.RisksCalculationApplication;
import ru.techlab.risks.calculation.RisksCalculationApplicationTests;
import ru.techlab.risks.calculation.config.AppConfig;
import ru.techlab.risks.calculation.config.ServicesConfig;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.techlab.risks.calculation.testmodel.TestLoanId;
import ru.techlab.risks.calculation.tmp.SampleEntityRepoTest;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.model.loan.LoanQuality;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 28.08.2017.
 */
@ContextConfiguration(classes = {ServicesConfig.class})
public class LoansDefs {
    @Autowired
    private LoansService loansService;
    @Autowired
    private DelayService delayService;
    @Autowired
    private QualityService qualityService;

    //@Autowired
    //private DelaysRepository delaysRepository;

    private BaseLoan latestLoan = null;
    private LoanQuality latestLoanQuality = null;

    /*@Given("^user pass datetime range for test \\(from: \"([^\"]*)\" till: \"([^\"]*)\"\\)$")
    public void aUserHasPostedTheFollowingDateTimeRange(String from, String till) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }*/

    @Given("^account number with no delays$")
    public void account_number_with_no_delays(@Transpose List<TestLoanId> loans) throws LoanNotFoundException {
        //Iterable<BaseDelay> delays = delaysRepository.findAll();

        TestLoanId testLoanId = loans.get(0);

        BaseLoan baseLoan = new BaseLoan();
        baseLoan.setBranch(testLoanId.getBranch());
        baseLoan.setLoanAccountNumber(testLoanId.getLoanAccountNumber());
        baseLoan.setLoanAccountSuffix(testLoanId.getLoanAccountSuffix());

        try {
            List<BaseDelay> test = delayService.getDelaysByLoan(baseLoan).collect(Collectors.toList());
        } catch (DelayNotFoundException e) {
            e.printStackTrace();
        }

        latestLoan = loansService.getActiveAndNonPortfolioLoan(testLoanId.getBranch(),testLoanId.getLoanAccountNumber(),testLoanId.getLoanAccountSuffix());
    }

    @When("^get no delays for specified account$")
    public void get_no_delays_for_specified_account() throws DelayNotFoundException{
        delayService.getDelaysByLoan(latestLoan);
    }

    @Then("^the client receives loan quality (.+)$")
    public void the_client_receives_loan_quality(@Transpose LoanQuality loanQuality) throws DelayNotFoundException {
        latestLoanQuality = qualityService.calculateLoanQuality(latestLoan);
        Assert.assertEquals(loanQuality, latestLoanQuality);
    }

}
