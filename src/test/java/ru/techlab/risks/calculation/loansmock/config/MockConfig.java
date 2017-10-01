package ru.techlab.risks.calculation.loansmock.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ru.techlab.risks.calculation.repository.CustomerRepository;
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
    public CustomerService customerService() {
        return Mockito.mock(CustomerService.class);
    }

    @Bean
    @Primary
    public QualityService qualityService(){
        return Mockito.mock(QualityService.class);
    }

    @Bean
    @Primary
    public LoansService loansService(){
        return Mockito.mock(LoansService.class);
    }

    @Bean
    @Primary
    public DelayService delayService(){
        return Mockito.mock(DelayService.class);
    }
}
