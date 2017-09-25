package ru.techlab.risks.calculation.services.loans;

import org.joda.time.LocalDateTime;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.model.loan.Loan;

import java.util.List;

/**
 * Created by rb052775 on 22.08.2017.
 */
public interface LoansService<V extends Loan> {
    List<V> getLoansByDtRangeAndActive(LocalDateTime dtFrom, LocalDateTime dtTill, boolean isActive) throws ConvertionEx;
    void process();
}
