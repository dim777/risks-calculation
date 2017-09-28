package ru.techlab.risks.calculation.loans;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.techlab.risks.calculation.testmodel.TestLoanId;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.model.delay.DelayType;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RisksCalculationApplicationGoodTests {
	@Autowired
	protected QualityService qualityService;
	@Autowired
	protected DelaysRepository delaysRepository;
	@Autowired
	protected LoansService loansService;
	@Autowired
	protected DelayService delayService;

	private BaseLoan latestLoan = null;

	/**
	 | branch | 5139 |
	 | loanAccountNumber | 045737 |
	 | loanAccountSuffix | 200 |
	 */
	@Test
	public void contextLoads() throws DelayNotFoundException {
		BaseDelay baseDelay0 = new BaseDelay();

		baseDelay0.setBranch("5139");
		baseDelay0.setLoanAccountNumber("045737");
		baseDelay0.setLoanAccountSuffix("200");
		baseDelay0.setLoanStartDate(1170811D);
		baseDelay0.setDelayType(DelayType.I);
		baseDelay0.setStartDate(1170814D);
		baseDelay0.setEndDate(9999999D);

		delaysRepository.save(baseDelay0);

		BaseDelay baseDelay1 = new BaseDelay();
		baseDelay1.setBranch("5139");
		baseDelay1.setLoanAccountNumber("045737");
		baseDelay1.setLoanAccountSuffix("200");
		baseDelay1.setLoanStartDate(1170811D);
		baseDelay1.setDelayType(DelayType.P);
		baseDelay1.setStartDate(1170814D);
		baseDelay1.setEndDate(9999999D);

		delaysRepository.save(baseDelay1);
	}

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
    public void account_number_with_no_delays() throws LoanNotFoundException {
        TestLoanId testLoanId = new TestLoanId();
        testLoanId.setBranch("5139");
        testLoanId.setLoanAccountNumber("045737");
        testLoanId.setLoanAccountSuffix("200");

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

	/**
	 * When: get no delays for specified account
	 */
	@Test
	public void get_no_delays_for_specified_account() throws LoanNotFoundException {

	}

	/**
	 * Then: the client receives loan quality GOOD
	 */
	@Test
	public void the_client_receives_loan_quality() throws LoanNotFoundException {

	}
}
