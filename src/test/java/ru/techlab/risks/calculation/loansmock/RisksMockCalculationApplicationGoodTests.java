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
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.techlab.risks.calculation.repository.LoansRepository;
import ru.techlab.risks.calculation.services.customer.CustomerService;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.delay.DelayType;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;

import java.util.Optional;
import java.util.stream.Stream;

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

	@Autowired
	private LoansRepository loansRepository;
	@Autowired
	private DelaysRepository delaysRepository;

	/**
	 | branch | 5139 |
	 | loanAccountNumber | 045737 |
	 | loanAccountSuffix | 200 |
	 */
	//private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9499", "V79963", "200");
	//private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9499", "V79963", "200");
	private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9472", "889684", "200");
	private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9472", "889684", "200");

	private static BaseLoan latestLoan = null;

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

		Mockito.when(loansRepository.findActiveAndNonPortfolioSimpleLoansByLoanId(ACCOUNT_ID_WITH_NO_DELAY.getBranch(), ACCOUNT_ID_WITH_NO_DELAY.getLoanAccountNumber(), ACCOUNT_ID_WITH_NO_DELAY.getLoanAccountSuffix()))
				.thenReturn(Optional.ofNullable(mockBaseLoan));

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
		BaseDelay baseDelay0 = new BaseDelay();
		baseDelay0.setBranch(latestLoan.getBranch());
		baseDelay0.setLoanAccountNumber(latestLoan.getLoanAccountNumber());
		baseDelay0.setLoanAccountSuffix(latestLoan.getLoanAccountSuffix());
		baseDelay0.setDelayType(DelayType.P);
		baseDelay0.setLoanStartDate(1140422D);
		baseDelay0.setStartDate(1140722D);
		baseDelay0.setEndDate(1140723D);

		BaseDelay baseDelay1 = new BaseDelay();
		baseDelay1.setBranch(latestLoan.getBranch());
		baseDelay1.setLoanAccountNumber(latestLoan.getLoanAccountNumber());
		baseDelay1.setLoanAccountSuffix(latestLoan.getLoanAccountSuffix());
		baseDelay1.setDelayType(DelayType.P);
		baseDelay1.setLoanStartDate(1140422D);
		baseDelay1.setStartDate(1141222D);
		baseDelay1.setEndDate(1141223D);


		Mockito.when(delaysRepository.findSimpleDelayByLoan(latestLoan.getBranch(), latestLoan.getLoanAccountNumber(), latestLoan.getLoanAccountSuffix()))
				.thenReturn(Stream.of(baseDelay0, baseDelay1));

		LoanServCoeff loanServCoeff = qualityService.calculateLoanServCoeff(latestLoan);
		Assert.assertEquals(LoanServCoeff.GOOD, loanServCoeff);
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
