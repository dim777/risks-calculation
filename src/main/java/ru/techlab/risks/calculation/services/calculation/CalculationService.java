package ru.techlab.risks.calculation.services.calculation;

import org.joda.time.LocalDateTime;
import ru.techlab.risks.calculation.model.LoanQualityResult;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;

import java.util.List;

/**
 * Created by rb052775 on 18.10.2017.
 */
public interface CalculationService {
    List<LoanQualityResult> calculateLQR(List<LoanQualityCategory> loanQualityCategories, final LocalDateTime END_OF_DATE);
}
