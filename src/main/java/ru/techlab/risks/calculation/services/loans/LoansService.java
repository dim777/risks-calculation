package ru.techlab.risks.calculation.services.loans;

import org.joda.time.LocalDateTime;
import ru.techlab.risks.calculation.model.AccountId;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 22.08.2017.
 */
public interface LoansService {
    BaseLoan getActiveAndNonPortfolioLoan(AccountId accountId) throws LoanNotFoundException, CustomerNotFoundEx;
    Stream<BaseLoan> getLoansByDtRangeAndActive(LocalDateTime dtFrom, LocalDateTime dtTill, boolean isActive) throws ConvertionEx;
    List<BaseLoan> getAllActiveAndNonPortfolioBaseLoans();
    //void process();
}
