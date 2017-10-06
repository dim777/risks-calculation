package ru.techlab.risks.calculation.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.techlab.risks.calculation.model.rest.BaseConfig;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;
import ru.techlab.risks.calculation.services.config.ConfigService;
import ru.techlab.risks.calculation.services.config.RiskConfigParamsService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by rb052775 on 06.10.2017.
 */
@Component
public class SetUp {
    @Autowired
    private RiskConfigParamsService riskConfigParamsService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private AppCache appCache;

    @PostConstruct
    public void setUpParams(){
        appCache.setVar("BASE_CONFIG", configService.getBaseConfig());
        appCache.setVar("LOAN_QUALITY_CATEGORIES", riskConfigParamsService.getAllLoanQualityCategories());
        appCache.setVar("LOAN_SERV_COEFF", riskConfigParamsService.getAllLoanServCoeffs());
        appCache.setVar("LOAN_QUALITY_CATEGORY_MATRIX", riskConfigParamsService.getAllLoanQualityCategoryMatrix());
    }
}
