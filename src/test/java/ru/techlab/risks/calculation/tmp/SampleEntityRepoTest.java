package ru.techlab.risks.calculation.tmp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.techlab.risks.calculation.CassandraRepositoryTests;
import ru.techlab.risks.calculation.RisksCalculationApplication;
import ru.techlab.risks.calculation.RisksCalculationApplicationTests;
import ru.techlab.risks.calculation.config.AppConfig;
import ru.techlab.risks.calculation.config.ServicesConfig;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;

import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.model.delay.DelayType;

import java.util.List;
import java.util.UUID;

/**
 * Created by rb052775 on 27.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SampleEntityRepoTest extends CassandraRepositoryTests {

    @Autowired
    protected DelaysRepository delaysRepository;

    @Before
    public void insert(){
        BaseDelay baseDelay0 = new BaseDelay();

        baseDelay0.setBranch("5139");
        baseDelay0.setLoanAccountNumber("045737");
        baseDelay0.setLoanAccountSuffix("200");
        baseDelay0.setLoanStartDate(1170811D);
        baseDelay0.setDelayType(DelayType.I);
        baseDelay0.setStartDate(1170814D);
        baseDelay0.setEndDate(9999999D);

        delaysRepository.save(baseDelay0);

        BaseDelay baseDelay1 = new BaseDelay();
        baseDelay1.setBranch("5139");
        baseDelay1.setLoanAccountNumber("045737");
        baseDelay1.setLoanAccountSuffix("200");
        baseDelay1.setLoanStartDate(1170811D);
        baseDelay1.setDelayType(DelayType.P);
        baseDelay1.setStartDate(1170814D);
        baseDelay1.setEndDate(9999999D);

        delaysRepository.save(baseDelay1);
    }

    @Test
    public void saveOneTest() {
        Iterable<BaseDelay> delays = delaysRepository.findAll();
    }
}