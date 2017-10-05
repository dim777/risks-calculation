package ru.techlab.risks.calculation.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;

import java.util.List;

/**
 * Created by rb052775 on 05.10.2017.
 */
@Service
public class RiskConfigParamsServiceImpl implements RiskConfigParamsService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.configServer}")
    private String configServer;
    @Value("${app.path.loanqualitycategories}")
    private String loanQualityCategories;
    @Value("${app.path.loanservcoeffs}")
    private String loanServCoeffs;
    @Value("${app.path.loanservcoeffsmatrix}")
    private String loanServCoeffsMatrix;

    @Override
    public List<LoanQualityCategory> getAllLoanQualityCategories() {
        ResponseEntity<List<LoanQualityCategory>> response =
                restTemplate.exchange(configServer + loanQualityCategories,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<LoanQualityCategory>>() {
                        });
        List<LoanQualityCategory> loanQualityCategories = response.getBody();
        return loanQualityCategories;
    }

    @Override
    public List<LoanServCoeff> getAllLoanServCoeffs() {
        ResponseEntity<List<LoanServCoeff>> responce =
                restTemplate.exchange(configServer + loanServCoeffs,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<LoanServCoeff>>() {
                        });
        List<LoanServCoeff> loanQualityCategories = responce.getBody();
        return loanQualityCategories;
    }

    @Override
    public List<LoanQualityCategoryMatrix> getAllLoanQualityCategoryMatrix() {
        ResponseEntity<List<LoanQualityCategoryMatrix>> responce =
                restTemplate.exchange(configServer + loanServCoeffsMatrix,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<LoanQualityCategoryMatrix>>() {
                        });
        List<LoanQualityCategoryMatrix> loanQualityCategoryMatrix = responce.getBody();
        return loanQualityCategoryMatrix;
    }
}
