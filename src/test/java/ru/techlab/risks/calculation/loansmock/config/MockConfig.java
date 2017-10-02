package ru.techlab.risks.calculation.loansmock.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ru.techlab.risks.calculation.repository.AccountRepository;
import ru.techlab.risks.calculation.repository.CustomerRepository;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.techlab.risks.calculation.repository.LoansRepository;
import ru.techlab.risks.calculation.services.customer.CustomerService;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;

/**
 * Created by dim777 on 30.09.17.
 */
@Configuration
@Profile("test")
public class MockConfig {
    @Bean
    @Primary
    public AccountRepository accountRepository() {
        return Mockito.mock(AccountRepository.class);
    }

    @Bean
    @Primary
    public CustomerRepository customerRepository(){
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    @Primary
    public DelaysRepository delaysRepository(){
        return Mockito.mock(DelaysRepository.class);
    }

    @Bean
    @Primary
    public LoansRepository loansRepository(){
        return Mockito.mock(LoansRepository.class);
    }
}
