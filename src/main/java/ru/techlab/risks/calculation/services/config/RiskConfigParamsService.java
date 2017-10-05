package ru.techlab.risks.calculation.services.config;

import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;

import java.util.List;

/**
 * Created by rb052775 on 05.10.2017.
 */
public interface RiskConfigParamsService {
    List<LoanQualityCategory> getAllLoanQualityCategories();
    List<LoanServCoeff> getAllLoanServCoeffs();
    List<LoanQualityCategoryMatrix> getAllLoanQualityCategoryMatrix();
}
