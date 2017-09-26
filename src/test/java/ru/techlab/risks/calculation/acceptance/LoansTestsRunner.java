package ru.techlab.risks.calculation.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by rb052775 on 28.08.2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/loans")
public class LoansTestsRunner {

}
