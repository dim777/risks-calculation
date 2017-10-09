package ru.techlab.risks.calculation.loans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.component.AppCache;
import ru.techlab.risks.calculation.component.AppCacheImpl;
import ru.techlab.risks.calculation.config.AppConfig;
import ru.techlab.risks.calculation.loans.config.TestAppConfig;
import ru.techlab.risks.calculation.model.AccountId;
import ru.techlab.risks.calculation.model.BaseCustomer;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.model.rest.BaseConfig;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;
import ru.techlab.risks.calculation.services.config.ConfigService;
import ru.techlab.risks.calculation.services.config.RiskConfigParamsService;
import ru.techlab.risks.calculation.services.customer.CustomerService;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanServCoeffNotFoundEx;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.loan.LoanServCoeffType;

import java.io.IOException;
import java.util.List;

/**
 * Created by rb052775 on 09.10.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestAppConfig.class,  AppConfig.class, AppCacheImpl.class})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AllLoansTest {
    @Autowired
    private QualityService qualityService;
    @Autowired
    private LoansService loansService;
    @Autowired
    private DelayService delayService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RiskConfigParamsService riskConfigParamsService;

    @Autowired
    private AppCache appCache;

    @Value("${app.configServer}")
    private String configServer;
    @Value("${app.path.config}")
    private String configPath;
    @Value("${app.path.loanqualitycategories}")
    private String loanQualityCategories;
    @Value("${app.path.loanservcoeffs}")
    private String loanServCoeffs;
    @Value("${app.path.loanservcoeffsmatrix}")
    private String loanServCoeffsMatrix;

    private static boolean IS_RUN_ONCE = true;
    private static int RUN_ONCE_COUNTER_BEFORE;
    private static int RUN_ONCE_COUNTER_AFTER;

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     | branch | 5139 |
     | loanAccountNumber | 045737 |
     | loanAccountSuffix | 200 |
     */
    //private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9499", "V79963", "200");
    //private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9499", "V79963", "200");
    private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9472", "889684", "200");
    private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9472", "889684", "200");
    private static final LocalDateTime TEST_END_OF_DATE = new LocalDateTime("2017-09-21");

    private static BaseLoan LATEST_LOAN = null;

	/*private static BaseConfig CONFIG = null;
	private static List<LoanServCoeff> LOAN_SERV_COEFFS = null;
	private static List<LoanQualityCategory> LOAN_QUALITY_CATEGORY = null;
	private static List<LoanQualityCategoryMatrix> LOAN_QUALITY_CATEGORY_MATRIX = null;*/

    /**
     * Обслуживание долга по ссуде может быть признано хорошим, если:
     * 1) платежи по основному долгу и процентам осуществляются своевременно и в полном объеме (нет просроченных платежей);
     * 2) имеется случай (имеются случаи) просроченных платежей по основному долгу и (или) процентам в течение последних 180 календарных дней:
     * 3) по ссудам, предоставленным юридическим лицам, - продолжительностью (общей продолжительностью) до 5 календарных дней включительно.
     *
     * Scenario: there are no delays for specified loan account
     *
     * Given: account number with no delays
     | branch | 5139 |
     | loanAccountNumber | 045737 |
     | loanAccountSuffix | 200 |
     */

    @Test
    public void a_get_config_params() throws IOException {
        String responseBody = "{\"id\":1,\"endOfDay\":\"2017-09-21\"}";

        BaseConfig mockConfig = mapper.readValue(responseBody, BaseConfig.class);

        Mockito
                .when(configService.getBaseConfig())
                .thenReturn(mockConfig);

        BaseConfig baseConfig = configService.getBaseConfig();
        appCache.setVar("BASE_CONFIG", baseConfig);
        Assert.assertEquals(Integer.valueOf(1), ((BaseConfig)appCache.getVar("BASE_CONFIG")).getId());
    }

    @Test
    public void b_get_risk_loan_serv_coeff() throws IOException {
        String responseBody = "[{\"type\":\"MID\",\"id\":5,\"isLegalEntitity\":false,\"forLastNDays\":180,\"moreOrEqThanDays\":30,\"lessThanDays\":60},{\"type\":\"GOOD\",\"id\":1,\"isLegalEntitity\":true,\"forLastNDays\":180,\"moreOrEqThanDays\":0,\"lessThanDays\":5},{\"type\":\"MID\",\"id\":2,\"isLegalEntitity\":true,\"forLastNDays\":180,\"moreOrEqThanDays\":5,\"lessThanDays\":30},{\"type\":\"GOOD\",\"id\":4,\"isLegalEntitity\":false,\"forLastNDays\":180,\"moreOrEqThanDays\":0,\"lessThanDays\":30},{\"type\":\"BAD\",\"id\":6,\"isLegalEntitity\":false,\"forLastNDays\":180,\"moreOrEqThanDays\":60,\"lessThanDays\":9999999},{\"type\":\"BAD\",\"id\":3,\"isLegalEntitity\":true,\"forLastNDays\":180,\"moreOrEqThanDays\":30,\"lessThanDays\":9999999}]";

        List<LoanServCoeff> mockLoanServCoeffs = mapper.readValue(responseBody,
                TypeFactory.defaultInstance().constructCollectionType(List.class,
                        LoanServCoeff.class));

        Mockito
                .when(riskConfigParamsService.getAllLoanServCoeffs())
                .thenReturn(mockLoanServCoeffs);

        List<LoanServCoeff> loanServCoeffs = riskConfigParamsService.getAllLoanServCoeffs();
        appCache.setVar("LOAN_SERV_COEFFS", loanServCoeffs);
        Assert.assertEquals(6, ((List<LoanServCoeff>)appCache.getVar("LOAN_SERV_COEFFS")).size());
    }

    @Test
    public void c_get_risk_loan_quality_categories() throws IOException {
        String responseBody = "[{\"id\":5,\"type\":\"HOPELESS\",\"pmin\":100.0},{\"id\":1,\"type\":\"STANDARD\",\"pmin\":0.0},{\"id\":2,\"type\":\"NONSTANDARD\",\"pmin\":1.0},{\"id\":4,\"type\":\"PROBLEM\",\"pmin\":51.0},{\"id\":3,\"type\":\"DOUBTFUL\",\"pmin\":21.0}]";

        List<LoanQualityCategory> mockLoanQualityCategory = mapper.readValue(responseBody,
                TypeFactory.defaultInstance().constructCollectionType(List.class, LoanQualityCategory.class));

        Mockito
                .when(riskConfigParamsService.getAllLoanQualityCategories())
                .thenReturn(mockLoanQualityCategory);

        List<LoanQualityCategory> loanQualityCategories = riskConfigParamsService.getAllLoanQualityCategories();
        appCache.setVar("LOAN_QUALITY_CATEGORIES", loanQualityCategories);
        Assert.assertEquals(5, ((List<LoanQualityCategory>)appCache.getVar("LOAN_QUALITY_CATEGORIES")).size());
    }

    @Test
    public void d_get_risk_loan_quality_category_matrix() throws IOException {
        String responseBody = "[{\"loanServCoeffId\":1,\"loanQualityByFsType1\":1,\"loanQualityByFsType2\":2,\"loanQualityByFsType3\":3},{\"loanServCoeffId\":2,\"loanQualityByFsType1\":2,\"loanQualityByFsType2\":3,\"loanQualityByFsType3\":4},{\"loanServCoeffId\":3,\"loanQualityByFsType1\":3,\"loanQualityByFsType2\":4,\"loanQualityByFsType3\":5}]";

        List<LoanQualityCategoryMatrix> mockLoanQualityCategoryMatrix = mapper.readValue(responseBody,
                TypeFactory.defaultInstance().constructCollectionType(List.class, LoanQualityCategoryMatrix.class));

        Mockito
                .when(riskConfigParamsService.getAllLoanQualityCategoryMatrix())
                .thenReturn(mockLoanQualityCategoryMatrix);

        List<LoanQualityCategoryMatrix> loanQualityCategoryMatrix = riskConfigParamsService.getAllLoanQualityCategoryMatrix();
        appCache.setVar("LOAN_QUALITY_CATEGORY_MATRIX", loanQualityCategoryMatrix);
        Assert.assertEquals(3, ((List<LoanQualityCategory>)appCache.getVar("LOAN_QUALITY_CATEGORY_MATRIX")).size());
    }



    @Test
    public void i_calculate_loan_quality_category() throws CustomerNotFoundEx, QualityConvertionEx, LoanServCoeffNotFoundEx {
        List<BaseLoan> loans = loansService.getAllActiveAndNonPortfolioBaseLoans();
        loans.forEach(loan -> {
            try {
                LoanServCoeffType loanServCoeff = qualityService.calculateLoanServCoeff(loan, TEST_END_OF_DATE);
                BaseCustomer customer = customerService.getCustomer(loan.getLoanAccountNumber());
                LoanQualityCategory loanQualityCategory = qualityService.calculateLoanQualityCategory(customer.FIN_STATE_TYPE(), loanServCoeff);
            } catch (LoanServCoeffNotFoundEx loanServCoeffNotFoundEx) {
                loanServCoeffNotFoundEx.printStackTrace();
            } catch (CustomerNotFoundEx customerNotFoundEx) {
                customerNotFoundEx.printStackTrace();
            } catch (QualityConvertionEx qualityConvertionEx) {
                qualityConvertionEx.printStackTrace();
            }
        });
    }
}
