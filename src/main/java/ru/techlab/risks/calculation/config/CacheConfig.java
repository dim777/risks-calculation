package ru.techlab.risks.calculation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;
import ru.techlab.risks.calculation.services.config.RiskConfigParamsService;

import java.util.List;

/**
 * Created by rb052775 on 05.10.2017.
 */
@Component
public class CacheConfig {
    @Autowired
    private RiskConfigParamsService riskConfigParamsService;

    @Bean
    public List<LoanQualityCategory> loanQualityCategoriesCache(){
        return riskConfigParamsService.getAllLoanQualityCategories();
    }

    @Bean
    public List<LoanServCoeff> loanServCoeffsCache(){
        return riskConfigParamsService.getAllLoanServCoeffs();
    }

    @Bean
    public List<LoanQualityCategoryMatrix> loanQualityCategoryMatrixCache(){
        return riskConfigParamsService.getAllLoanQualityCategoryMatrix();
    }
}
