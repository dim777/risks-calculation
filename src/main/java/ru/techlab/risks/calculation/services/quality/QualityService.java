package ru.techlab.risks.calculation.services.quality;

import ru.techlab.risks.calculation.model.BaseLoan;
import ru.xegex.risks.libs.model.customer.FinState;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;

/**
 * Created by rb052775 on 27.09.2017.
 */
public interface QualityService {
    LoanServCoeff calculateLoanServCoeff(BaseLoan loan);
    LoanQualityCategory calculateLoanQualityCategory(FinState finState, LoanServCoeff loanQuality);
}
