package ru.techlab.risks.calculation.services.delay;

import ru.techlab.risks.calculation.model.SimpleDelay;
import ru.techlab.risks.calculation.model.SimpleLoan;
import ru.xegex.risks.libs.ex.loans.NoLoansFoundEx;
import ru.xegex.risks.libs.model.delay.Delay;
import ru.xegex.risks.libs.model.loan.Loan;

import java.util.List;
import java.util.Optional;

/**
 * Created by rb052775 on 26.09.2017.
 */
public interface DelayService {
    List<SimpleDelay> getDelaysByLoan(SimpleLoan loan) throws NoLoansFoundEx;
}
