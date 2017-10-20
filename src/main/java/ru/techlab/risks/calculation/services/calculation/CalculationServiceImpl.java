package ru.techlab.risks.calculation.services.calculation;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseCustomer;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.model.LoanQualityResult;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.repository.LoanQualityResultRepository;
import ru.techlab.risks.calculation.services.customer.CustomerService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.loans.LoanServCoeffNotFoundEx;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.loan.LoanServCoeffResult;
import ru.xegex.risks.libs.model.loan.LoanServCoeffType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Created by rb052775 on 18.10.2017.
 */
@Service
public class CalculationServiceImpl implements CalculationService {
    @Autowired
    private QualityService qualityService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanQualityResultRepository loanQualityResultRepository;

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public List<LoanQualityResult> calculateLQR(List<BaseLoan> loans, List<LoanQualityCategory> loanQualityCategories, final LocalDateTime END_OF_DATE) {
        List<LoanQualityResult> loanQualityResults = new ArrayList<>();

        loans.forEach(loan -> {
            try {
                LoanServCoeffResult loanServCoeffResult = qualityService.calculateLoanServCoeff(loan, END_OF_DATE);
                BaseCustomer customer = customerService.getCustomer(loan.getLoanAccountNumber());
                LoanQualityCategory loanQualityCategory = qualityService.calculateLoanQualityCategory(customer.FIN_STATE_TYPE(), loanServCoeffResult.getLoanServCoeffType());

                LoanQualityResult loanQualityResult = new LoanQualityResult();
                loanQualityResult.setBranch(loan.getBranch());
                loanQualityResult.setLoanAccountNumber(loan.getLoanAccountNumber());
                loanQualityResult.setLoanAccountSuffix(loan.getLoanAccountSuffix());
                loanQualityResult.setCustomerName(customer.getName());
                loanQualityResult.setStartDate(formatter.print(loan.getStartDate()));
                loanQualityResult.setBalance(loan.getBalance());
                loanQualityResult.setLoanServCoeffType(loanServCoeffResult.getLoanServCoeffType());
                loanQualityResult.setCalcDelayDays(loanServCoeffResult.getCalcDelayDays());
                loanQualityResult.setTotalDelayDays(loanServCoeffResult.getTotalDelayDays());
                loanQualityResult.setFinState(customer.FIN_STATE_TYPE());
                loanQualityResult.setLoanQualityCategory(loanQualityCategory.getId());
                loanQualityResult.setLoanQualityCategoryForAllCustomerLoans(loanQualityCategory.getId());
                loanQualityResult.setInterestRate(loanQualityCategory.getPMin());
                loanQualityResult.setInterestRateAll(loanQualityCategory.getPMin());

                loanQualityResults.add(loanQualityResult);

            } catch (LoanServCoeffNotFoundEx loanServCoeffNotFoundEx) {
                loanServCoeffNotFoundEx.printStackTrace();
            } catch (CustomerNotFoundEx customerNotFoundEx) {
                customerNotFoundEx.printStackTrace();
            } catch (QualityConvertionEx qualityConvertionEx) {
                qualityConvertionEx.printStackTrace();
            }
        });

        Collection<LoanQualityResult> loanQualityResultsDistincts = loanQualityResults
                .stream()
                .collect(toMap(LoanQualityResult::getCustomerName, p -> p, (p, q) -> p)).values();

        loanQualityResultsDistincts.forEach(
                loanQualityResultDistinct -> {
                    List<LoanQualityResult> allLoanQualityResult4Customer = loanQualityResults
                            .stream()
                            .filter(r -> r.getCustomerName().equals(loanQualityResultDistinct.getCustomerName()))
                            .collect(Collectors.toList());

                    Integer max = allLoanQualityResult4Customer
                            .stream()
                            .mapToInt(res -> res.getLoanQualityCategory())
                            .max()
                            .getAsInt();

                    allLoanQualityResult4Customer.forEach( r -> {
                        r.setLoanQualityCategoryForAllCustomerLoans(max);

                        r.setInterestRateAll(
                                loanQualityCategories
                                        .stream()
                                        .filter(cat -> cat.getId().equals(max))
                                        .findFirst()
                                        .get()
                                        .getPMin()
                        );
                    });
                    loanQualityResultRepository.save(allLoanQualityResult4Customer);
                }
        );

        return loanQualityResults;
    }
}
