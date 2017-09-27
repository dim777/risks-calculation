package ru.techlab.risks.calculation.services.delay;

import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 26.09.2017.
 */
public interface DelayService {
    Stream<BaseDelay> getDelaysByLoan(BaseLoan loan) throws DelayNotFoundException;
}
