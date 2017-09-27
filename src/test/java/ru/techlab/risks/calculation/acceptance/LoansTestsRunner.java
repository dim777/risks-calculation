package ru.techlab.risks.calculation.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.techlab.risks.calculation.CassandraRepositoryTests;
import ru.techlab.risks.calculation.config.AppConfig;
import ru.techlab.risks.calculation.tmp.SampleEntityRepoTest;

/**
 * Created by rb052775 on 28.08.2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/loans")
public class LoansTestsRunner extends SampleEntityRepoTest{

}
