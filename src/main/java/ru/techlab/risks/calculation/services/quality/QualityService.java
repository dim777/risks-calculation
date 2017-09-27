package ru.techlab.risks.calculation.services.quality;

import ru.techlab.risks.calculation.model.BaseLoan;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.model.loan.LoanQuality;

/**
 * Created by rb052775 on 27.09.2017.
 */
public interface QualityService {
    LoanQuality calculateLoanQuality(BaseLoan loan) throws DelayNotFoundException;
}
