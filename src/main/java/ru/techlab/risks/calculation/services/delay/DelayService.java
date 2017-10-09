package ru.techlab.risks.calculation.services.delay;

import org.joda.time.LocalDateTime;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;

import java.util.List;

/**
 * Created by rb052775 on 26.09.2017.
 */
public interface DelayService {
    List<BaseDelay> getDelaysByLoanForLastNDays(BaseLoan loan, LocalDateTime currentDate, int days) throws DelayNotFoundException;
}
