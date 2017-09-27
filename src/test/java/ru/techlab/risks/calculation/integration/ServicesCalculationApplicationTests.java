package ru.techlab.risks.calculation.integration;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.model.loan.Loan;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 22.08.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ServicesCalculationApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicesCalculationApplicationTests.class);

    @Autowired
    private LoansService loansService;

    @Value("${batch.maxchunk}")
    private int maxChunk;

    /**
     * Integration services tests section
     */
    @Test
    @Timed(millis = 60000)
    public void whenGetLoansServiceIsActiveAndBetweenSpecificDatesThenCountThem() throws ConvertionEx {
        LocalDateTime dtFrom = LocalDateTime.parse("2016-02-01");
        LocalDateTime dtTill = LocalDateTime.parse("2016-07-21");
        Stream<BaseLoan> specifiedActiveLoans = loansService.getLoansByDtRangeAndActive(dtFrom, dtTill, true);
        Assert.assertEquals(172, specifiedActiveLoans.count(), 0.0000001);
    }
    @Test
    @Timed(millis = 60000)
    /**
     * TODO: should configure cdc/kafka connect and create cass.tbl with cluster key on active(:ddact)
     */
    public void whenGetLoansServiceBetweenSpecificDatesThenCountThem() throws ConvertionEx {
        LocalDateTime dtFrom = LocalDateTime.parse("2016-02-01");
        LocalDateTime dtTill = LocalDateTime.parse("2016-07-21");
        Stream<BaseLoan> specifiedLoans = loansService.getLoansByDtRangeAndActive(dtFrom, dtTill, false);
        Assert.assertEquals(628466, specifiedLoans.count(), 0.0000001);
    }
}
