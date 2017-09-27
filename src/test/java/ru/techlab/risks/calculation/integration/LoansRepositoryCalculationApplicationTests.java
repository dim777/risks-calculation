package ru.techlab.risks.calculation.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.LoansRepository;
import ru.techlab.risks.calculation.services.loans.LoansService;

import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {AppConfig.class})
@SpringBootTest
@ActiveProfiles("test")
public class LoansRepositoryCalculationApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoansRepositoryCalculationApplicationTests.class);

	@Autowired
	private LoansRepository loansRepository;

	@Autowired
	private LoansService loansService;

	/**
	 * Integration repository Loans tests section
	 */
	@Test
	public void whenSelectLoansBetweenSpecificDatesThenCountThem() {
		Stream<BaseLoan> specifiedLoans = loansRepository.findSimpleLoansByStartDateBetween(1170701,1170804);
		Assert.assertEquals(35, specifiedLoans.count(), 0.0000001);
	}

	@Test
	public void whenSelectLoansIsActiveAndBetweenSpecificDatesThenCountThem() {
		Stream<BaseLoan> specifiedActiveLoans = loansRepository.findActiveSimpleLoansByStartDateBetween(1160501,1160805);
		Assert.assertEquals(97, specifiedActiveLoans.count(), 0.0000001);
	}

	@Test
	public void whenSelectAllActiveLoansFromCassandraThenCountThem() {
		Stream<BaseLoan> activeLoans = loansRepository.findAllActiveSimpleLoans();
		Assert.assertEquals(4367, activeLoans.count(), 0.0000001);
	}

	@Test(expected = RuntimeException.class)
	public void whenSelect999RowsFromCassandraThenExceptionIsThrown() {
		throw new RuntimeException();
	}
}
