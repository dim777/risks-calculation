package ru.techlab.risks.calculation.services.delay;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;
import ru.xegex.risks.libs.utils.DateTimeUtils;

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
    public List<BaseDelay> getDelaysByLoanForLastNDays(BaseLoan loan, LocalDateTime currentDate, int days) throws DelayNotFoundException {
        Stream<BaseDelay> stream = delaysRepository.findSimpleDelayByLoan(loan.getBranch(), loan.getLoanAccountNumber(), loan.getLoanAccountSuffix());
        List<BaseDelay> delays = stream
                .filter(baseDelay -> {
                    if(baseDelay.getFinishDelayDate().equals(new LocalDateTime(Integer.MAX_VALUE))) return true;
                    else if(DateTimeUtils.differenceInDays(baseDelay.getFinishDelayDate(), currentDate) > days) return false;
                    return true;
                })
                .collect(Collectors.toList());
        if(delays.size() == 0){
            throw new DelayNotFoundException("No delays found");
        }
        return delays;
    }
}
