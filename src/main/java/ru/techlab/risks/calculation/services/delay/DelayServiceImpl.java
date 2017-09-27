package ru.techlab.risks.calculation.services.delay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 26.09.2017.
 */
@Service
public class DelayServiceImpl implements DelayService{
    @Autowired
    private DelaysRepository delaysRepository;

    @Override
    public Stream<BaseDelay> getDelaysByLoan(BaseLoan loan) throws DelayNotFoundException {
        Stream<BaseDelay> stream = delaysRepository.findSimpleDelayByLoan(loan.getBranch(), loan.getLoanAccountNumber(), loan.getLoanAccountSuffix());
        if(stream.count() == 0){
            throw new DelayNotFoundException("No delays found");
        }
        return stream;
    }
}
