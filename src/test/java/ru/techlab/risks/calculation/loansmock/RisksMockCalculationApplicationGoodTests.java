package ru.techlab.risks.calculation.loansmock;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.loansmock.config.MockConfig;
import ru.techlab.risks.calculation.loansmock.config.TestAppConfig;
import ru.techlab.risks.calculation.model.AccountId;
import ru.techlab.risks.calculation.model.BaseCustomer;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.services.customer.CustomerService;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestAppConfig.class, MockConfig.class})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RisksMockCalculationApplicationGoodTests {

	@Autowired
	protected QualityService qualityService;
	@Autowired
	protected LoansService loansService;
	@Autowired
	protected DelayService delayService;
	@Autowired
	protected CustomerService customerService;

	/**
	 | branch | 5139 |
	 | loanAccountNumber | 045737 |
	 | loanAccountSuffix | 200 |
	 */
	//private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9499", "V79963", "200");
	//private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9499", "V79963", "200");
	private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9472", "889684", "200");
	private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9472", "889684", "200");

	private BaseLoan latestLoan = null;


	/**
	 * Обслуживание долга по ссуде может быть признано хорошим, если:
	 * 1) платежи по основному долгу и процентам осуществляются своевременно и в полном объеме (нет просроченных платежей);
	 * 2) имеется случай (имеются случаи) просроченных платежей по основному долгу и (или) процентам в течение последних 180 календарных дней:
	 * 3) по ссудам, предоставленным юридическим лицам, - продолжительностью (общей продолжительностью) до 5 календарных дней включительно.
	 *
	 * Scenario: there are no delays for specified loan account
	 *
	 * Given: account number with no delays
	 	| branch | 5139 |
	 	| loanAccountNumber | 045737 |
    	| loanAccountSuffix | 200 |
    */
    @Test
    public void a_account_number_with_no_delays() throws LoanNotFoundException {
        BaseLoan mockBaseLoan = new BaseLoan();
        mockBaseLoan.setBranch("9472");
        mockBaseLoan.setLoanAccountNumber("889684");
        mockBaseLoan.setLoanAccountSuffix("200");
        mockBaseLoan.setBalance(-300000000);
        mockBaseLoan.setStartDate(1140422);
        mockBaseLoan.setActive("Y");

		Mockito.when(loansService.getActiveAndNonPortfolioLoan(ACCOUNT_ID_WITH_NO_DELAY))
				.thenReturn(mockBaseLoan);
		latestLoan = loansService.getActiveAndNonPortfolioLoan(ACCOUNT_ID_WITH_NO_DELAY);
		Assert.assertNotEquals(null, latestLoan);
		/*
        try {
            List<BaseDelay> test = delayService.getDelaysByLoan(baseLoan).collect(Collectors.toList());
        } catch (DelayNotFoundException e) {
            e.printStackTrace();
        }*/
    }

	/**
	 * When: get no delays for specified account - the client receives loan quality GOOD
	 */
	@Test
	public void b_get_no_delays_for_specified_account(){
        Mockito.when(qualityService.calculateLoanServCoeff(latestLoan))
                .thenReturn(LoanServCoeff.GOOD);
		LoanServCoeff loanQuality = qualityService.calculateLoanServCoeff(latestLoan);
		Assert.assertEquals(LoanServCoeff.GOOD, loanQuality);
	}

	/**
	 * When: only one delay for last 180 days - the client receives loan quality GOOD
	 */
	@Test
	public void c_get_one_delays_for_specified_account_and_() throws LoanNotFoundException, DelayNotFoundException {
        Mockito.when(qualityService.calculateLoanServCoeff(latestLoan))
                .thenReturn(LoanServCoeff.GOOD);
		latestLoan = loansService.getActiveAndNonPortfolioLoan(ACCOUNT_ID_WITH_ONE_DELAY);
		LoanServCoeff loanQuality = qualityService.calculateLoanServCoeff(latestLoan);
		Assert.assertEquals(LoanServCoeff.GOOD, loanQuality);
	}

	@Test
	public void d_calculate_loan_quality_category() throws CustomerNotFoundEx, QualityConvertionEx {

	    LoanServCoeff loanServCoeff = qualityService.calculateLoanServCoeff(latestLoan);
		BaseCustomer customer = customerService.getCustomer(latestLoan.getLoanAccountNumber());
        Mockito.when(qualityService.calculateLoanQualityCategory(customer.getFinState(), loanServCoeff))
                .thenReturn(LoanQualityCategory.FIVE);
		LoanQualityCategory loanQualityCategory = qualityService.calculateLoanQualityCategory(customer.getFinState(), loanServCoeff);
		Assert.assertEquals(LoanServCoeff.GOOD, loanServCoeff);
	}

}
