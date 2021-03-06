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
import ru.techlab.risks.calculation.model.BaseAccount;
import ru.techlab.risks.calculation.repository.AccountRepository;
import ru.xegex.risks.libs.ex.account.AccountEx;

import java.util.Optional;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {AppConfig.class})
@SpringBootTest
@ActiveProfiles("test")
public class AccountsRepositoryCalculationApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountsRepositoryCalculationApplicationTests.class);

	@Autowired
	private AccountRepository accountRepository;

	/**
	 * Integration repository Accounts tests section
	 */
	@Test
	public void whenSelectAccountByBranchAndNumberAndSuffixThenGetOptional() throws AccountEx {
		BaseAccount account = accountRepository
				.findByBranchAndAccountNumberAndAccountSuffix("9370","V56287","405")
				.get();
		Assert.assertEquals(Integer.valueOf(3000000), account.getId());
	}
	@Test(expected = AccountEx.class)
	public void whenSelectAccountByBranchAndNumberAndSuffixThenThrowExceptionWhenCannotFind() throws AccountEx {
		Optional<BaseAccount> account = accountRepository.findByBranchAndAccountNumberAndAccountSuffix("9371","V56287","405");
		account.orElseThrow(()-> new AccountEx("Couldn't find specific account"));
	}
}
