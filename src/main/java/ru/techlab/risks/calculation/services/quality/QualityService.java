package ru.techlab.risks.calculation.services.quality;

import org.joda.time.LocalDateTime;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.xegex.risks.libs.ex.loans.LoanServCoeffNotFoundEx;
import ru.xegex.risks.libs.model.customer.FinStateType;
import ru.xegex.risks.libs.model.loan.LoanServCoeffType;

/**
 * Created by rb052775 on 27.09.2017.
 */
public interface QualityService {
    LoanServCoeffType calculateLoanServCoeff(BaseLoan loan, LocalDateTime localDateTime) throws LoanServCoeffNotFoundEx;
    LoanQualityCategory calculateLoanQualityCategory(FinStateType finStateType, LoanServCoeffType loanServCoeffType);
}
