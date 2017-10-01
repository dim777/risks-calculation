package ru.techlab.risks.calculation.loansmock.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by dim777 on 02.10.17.
 */
@Configuration
//@ComponentScan(basePackages = { "ru.techlab.risks.calculation.services.*" })
@Profile("test")
public class TestAppConfig {
}
