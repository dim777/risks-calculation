package ru.techlab.risks.calculation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rb052775 on 27.09.2017.
 */
@Configuration
@ComponentScan(basePackages = {"ru.techlab.risks.calculation.services","ru.techlab.risks.calculation.interfaces"})
public class ServicesConfig {
}
