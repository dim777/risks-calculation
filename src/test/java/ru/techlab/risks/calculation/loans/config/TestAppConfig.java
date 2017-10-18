package ru.techlab.risks.calculation.loans.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import ru.techlab.risks.calculation.services.config.ConfigService;
import ru.techlab.risks.calculation.services.config.RiskConfigParamsService;

/**
 * Created by rb052775 on 06.10.2017.
 */
@Configuration
@ComponentScan({
        "ru.techlab.risks.calculation.repository.*",
        "ru.techlab.risks.calculation.services.customer",
        "ru.techlab.risks.calculation.services.delay",
        "ru.techlab.risks.calculation.services.loans",
        "ru.techlab.risks.calculation.services.quality",
        "ru.techlab.risks.calculation.services.calculation"
})
@Profile("test")
public class TestAppConfig {
    private static ObjectMapper mapper = new ObjectMapper();

    @Bean
    @Primary
    public ConfigService configService(){
        return Mockito.mock(ConfigService.class);
    }

    @Bean
    @Primary
    public RiskConfigParamsService riskConfigParamsService(){
        return Mockito.mock(RiskConfigParamsService.class);
    }
}
