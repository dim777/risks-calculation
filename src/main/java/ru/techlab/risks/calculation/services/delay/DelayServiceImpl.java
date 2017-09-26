package ru.techlab.risks.calculation.services.delay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.SimpleDelay;
import ru.techlab.risks.calculation.model.SimpleLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.xegex.risks.libs.ex.loans.NoLoansFoundEx;

import java.util.List;
import java.util.Optional;
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
    public List<SimpleDelay> getDelaysByLoan(SimpleLoan loan) throws NoDelaysFoundEx {
        Stream<SimpleDelay> stream = delaysRepository.findSimpleDelayByLoan(loan.getBranch(), loan.getLoanAccountNumber(), loan.getLoanAccountSuffix());
        if(stream.count() == 0){
            throw new NoLoansFoundEx("No loans found");
        }
        return stream.collect(Collectors.toList());
    }
}
