package ru.techlab.risks.calculation.services.quality;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.component.AppCache;
import ru.techlab.risks.calculation.model.BaseCustomer;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanServCoeffNotFoundEx;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.customer.FinStateType;
import ru.xegex.risks.libs.model.loan.LoanServCoeffType;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.util.List;

/**
 * Created by rb052775 on 27.09.2017.
 */
@Service
public class QualityServiceImpl implements QualityService {
    @Autowired
    private DelayService delayService;

    @Autowired
    private AppCache appCache;

    private LoanServCoeffType calcCoeffType(BaseLoan loan, LocalDateTime endOfDay, List<LoanServCoeff> loanServCoeffs, Boolean isUr) throws LoanServCoeffNotFoundEx {

        LoanServCoeff loanServCoeffBad = loanServCoeffs
                .stream()
                .filter(c -> c.getType().equals(LoanServCoeffType.BAD) && c.getIsLegalEntitity().equals(isUr))
                .findFirst()
                .orElseThrow(() -> new LoanServCoeffNotFoundEx("Couldn't find coeff in loanServCoeffsCache"));

        LoanServCoeff loanServCoeffMid = loanServCoeffs
                .stream()
                .filter(c -> c.getType().equals(LoanServCoeffType.MID) && c.getIsLegalEntitity().equals(isUr))
                .findFirst()
                .orElseThrow(() -> new LoanServCoeffNotFoundEx("Couldn't find coeff in loanServCoeffsCache"));

        LoanServCoeff loanServCoeffGood = loanServCoeffs
                .stream()
                .filter(c -> c.getType().equals(LoanServCoeffType.GOOD) && c.getIsLegalEntitity().equals(isUr))
                .findFirst()
                .orElseThrow(() -> new LoanServCoeffNotFoundEx("Couldn't find coeff in loanServCoeffsCache"));

        List<BaseDelay> delaysList;
        try {
            delaysList = delayService.getDelaysByLoanForLastNDays(loan, endOfDay, loanServCoeffBad.getForLastNDays());
        } catch (DelayNotFoundException e) {
            return LoanServCoeffType.GOOD;
        }

        long countInfBad = delaysList
                .stream()
                .filter(baseDelay -> baseDelay.getFinishDelayDate().equals(new LocalDateTime(Integer.MAX_VALUE)))
                .filter(baseDelay -> {
                    int diffDays = DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), endOfDay);
                    if( diffDays >= loanServCoeffBad.getMoreOrEqThanDays() && diffDays < loanServCoeffBad.getLessThanDays()) return true;
                    return false;
                })
                .count();

        if(countInfBad > 0) return LoanServCoeffType.BAD;

        long countInfMid = delaysList
                .stream()
                .filter(baseDelay -> baseDelay.getFinishDelayDate().equals(new LocalDateTime(Integer.MAX_VALUE)))
                .filter(baseDelay -> {
                    int diffDays = DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), endOfDay);
                    if( diffDays >= loanServCoeffMid.getMoreOrEqThanDays() && diffDays < loanServCoeffMid.getLessThanDays()) return true;
                    return false;
                })
                .count();

        if(countInfMid > 0) return LoanServCoeffType.MID;

        long countInfGood = delaysList
                .stream()
                .filter(baseDelay -> baseDelay.getFinishDelayDate().equals(new LocalDateTime(Integer.MAX_VALUE)))
                .filter(baseDelay -> {
                    int diffDays = DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), endOfDay);
                    if( diffDays >= loanServCoeffGood.getMoreOrEqThanDays() && diffDays < loanServCoeffGood.getLessThanDays()) return true;
                    return false;
                })
                .count();

        if(countInfGood > 0) return LoanServCoeffType.GOOD;

        /**
         * Rest
         */
        long countBad = delaysList
                .stream()
                .filter(baseDelay -> {
                    int diffDays = DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), baseDelay.getFinishDelayDate());
                    if( diffDays >= loanServCoeffBad.getMoreOrEqThanDays() && diffDays < loanServCoeffBad.getLessThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanServCoeffType.BAD;

        long countMid = delaysList
                .stream()
                .filter(baseDelay -> {
                    int diffDays = DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), baseDelay.getFinishDelayDate());
                    if( diffDays >= loanServCoeffMid.getMoreOrEqThanDays() && diffDays < loanServCoeffMid.getLessThanDays()) return true;
                    return false;
                })
                .count();

        if(countMid > 0) return LoanServCoeffType.MID;

        long countGood = delaysList
                .stream()
                .filter(baseDelay -> {
                    int diffDays = DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), baseDelay.getFinishDelayDate());
                    if( diffDays >= loanServCoeffGood.getMoreOrEqThanDays() && diffDays < loanServCoeffGood.getLessThanDays()) return true;
                    return false;
                })
                .count();

        if(countGood > 0) return LoanServCoeffType.GOOD;

        return LoanServCoeffType.GOOD;
    }


    /**
     * Calculate LoanServCoeffType
     * @param loan
     * @param endOfDay
     * @return
     */
    @Override
    public LoanServCoeffType calculateLoanServCoeff(BaseLoan loan, LocalDateTime endOfDay) throws LoanServCoeffNotFoundEx, CustomerNotFoundEx {
        BaseCustomer customer = loan.getBaseCustomer().orElseThrow(() -> new CustomerNotFoundEx("Customer not found ex"));
        switch (customer.LEGAL_ENTITITY_TYPE()){
            case UR: return calcCoeffType(loan, endOfDay, (List<LoanServCoeff>)appCache.getVar("LOAN_SERV_COEFFS"), true);
            case PHY: return calcCoeffType(loan, endOfDay, (List<LoanServCoeff>)appCache.getVar("LOAN_SERV_COEFFS"), false);
        }
        throw new LoanServCoeffNotFoundEx("Error in LEGAL_ENTITITY_TYPE");
    }

    /**
     * Calculate LoanQualityCategory
     * @param finStateType
     * @param loanServCoeffType
     * @return
     */
    @Override
    public LoanQualityCategory calculateLoanQualityCategory(FinStateType finStateType, LoanServCoeffType loanServCoeffType) throws QualityConvertionEx {
        List<LoanQualityCategoryMatrix> loanQualityCategoryMatrix = (List<LoanQualityCategoryMatrix>)appCache.getVar("LOAN_QUALITY_CATEGORY_MATRIX");
        List<LoanQualityCategory> loanQualityCategories = (List<LoanQualityCategory>)appCache.getVar("LOAN_QUALITY_CATEGORIES");

        int x = 0;
        if(loanServCoeffType == LoanServCoeffType.GOOD) x = 0;
        else if(loanServCoeffType == LoanServCoeffType.MID) x = 1;
        else if(loanServCoeffType == LoanServCoeffType.BAD) x = 2;

        if(finStateType == FinStateType.GOOD) {
            final Integer kkcVal = loanQualityCategoryMatrix.get(x).getLoanQualityByFsType1();
            LoanQualityCategory loanQualityCategory = loanQualityCategories
                    .stream()
                    .filter(lqc -> lqc.getId().equals(kkcVal))
                    .findFirst()
                    .orElseThrow(() -> new QualityConvertionEx("Loan Quality Category mapping failure"));
            return loanQualityCategory;
        }
        else if(finStateType == FinStateType.MIDDLE) {
            final Integer kkcVal = loanQualityCategoryMatrix.get(x).getLoanQualityByFsType2();
            LoanQualityCategory loanQualityCategory = loanQualityCategories
                    .stream()
                    .filter(lqc -> lqc.getId().equals(kkcVal))
                    .findFirst()
                    .orElseThrow(() -> new QualityConvertionEx("Loan Quality Category mapping failure"));
            return loanQualityCategory;
        }
        else if(finStateType == FinStateType.UNSATISFACTORY) {
            final Integer kkcVal = loanQualityCategoryMatrix.get(x).getLoanQualityByFsType3();
            LoanQualityCategory loanQualityCategory = loanQualityCategories
                    .stream()
                    .filter(lqc -> lqc.getId().equals(kkcVal))
                    .findFirst()
                    .orElseThrow(() -> new QualityConvertionEx("Loan Quality Category mapping failure"));
            return loanQualityCategory;
        }

        return loanQualityCategories
                .stream()
                .filter(lqc -> lqc.getId().equals(5))
                .findFirst()
                .orElseThrow(() -> new QualityConvertionEx("Loan Quality Category mapping failure"));
    }
}
