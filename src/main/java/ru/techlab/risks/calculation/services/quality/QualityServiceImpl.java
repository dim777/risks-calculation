package ru.techlab.risks.calculation.services.quality;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;
import ru.techlab.risks.calculation.services.config.RiskConfigParamsService;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanServCoeffNotFoundEx;
import ru.xegex.risks.libs.model.customer.FinStateType;
import ru.xegex.risks.libs.model.loan.LoanServCoeffType;

import java.util.List;
import java.util.Optional;

/**
 * Created by rb052775 on 27.09.2017.
 */
@Service
public class QualityServiceImpl implements QualityService {
    @Autowired
    private DelayService delayService;
    @Autowired
    private RiskConfigParamsService riskConfigParamsService;
    @Autowired
    private List<LoanQualityCategory> loanQualityCategoriesCache;
    @Autowired
    private List<LoanServCoeff> loanServCoeffsCache;
    @Autowired
    private List<LoanQualityCategoryMatrix> loanQualityCategoryMatrixCache;

    /**
     * Calculate LoanServCoeffType
     * @param loan
     * @param endOfDay
     * @return
     */
    @Override
    public LoanServCoeffType calculateLoanServCoeff(BaseLoan loan, LocalDateTime endOfDay) throws LoanServCoeffNotFoundEx {
        LoanServCoeff loanServCoeffBad = loanServCoeffsCache
                .stream()
                .filter(c -> c.getType().equals(LoanServCoeffType.BAD))
                .findFirst()
                .orElseThrow(() -> new LoanServCoeffNotFoundEx("Couldn't find coeff in loanServCoeffsCache"));

        LoanServCoeff loanServCoeffMid = loanServCoeffsCache
                .stream()
                .filter(c -> c.getType().equals(LoanServCoeffType.MID))
                .findFirst()
                .orElseThrow(() -> new LoanServCoeffNotFoundEx("Couldn't find coeff in loanServCoeffsCache"));

        LoanServCoeff loanServCoeffGood = loanServCoeffsCache
                .stream()
                .filter(c -> c.getType().equals(LoanServCoeffType.GOOD))
                .findFirst()
                .orElseThrow(() -> new LoanServCoeffNotFoundEx("Couldn't find coeff in loanServCoeffsCache"));

        List<BaseDelay> delaysList;
        try {
            delaysList = delayService.getDelaysByLoanForLastNDays(loan, endOfDay, loanServCoeffBad.getLastDays());
        } catch (DelayNotFoundException e) {
            return LoanServCoeffType.GOOD;
        }

        long countBad = delaysList
                .stream()
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > loanServCoeffBad.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanServCoeffType.BAD;

        long countMid = delaysList
                .stream()
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > loanServCoeffMid.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countMid > 0) return LoanServCoeffType.MID;

        long countGood = delaysList
                .stream()
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > loanServCoeffGood.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanServCoeffType.GOOD;

        return LoanServCoeffType.GOOD;
    }

    /**
     * Calculate LoanQualityCategory
     * @param finStateType
     * @param loanServCoeffType
     * @return
     */
    @Override
    public LoanQualityCategory calculateLoanQualityCategory(FinStateType finStateType, LoanServCoeffType loanServCoeffType){
        int x = 0;
        if(loanServCoeffType == LoanServCoeffType.GOOD) x = 0;
        else if(loanServCoeffType == LoanServCoeffType.MID) x = 1;
        else if(loanServCoeffType == LoanServCoeffType.BAD) x = 2;

        if(finStateType == FinStateType.GOOD) {
            return loanQualityCategoriesCache.get(loanQualityCategoryMatrixCache.get(x).getLoanQualityByFsType1());
        }
        else if(finStateType == FinStateType.MIDDLE) {
            return loanQualityCategoriesCache.get(loanQualityCategoryMatrixCache.get(x).getLoanQualityByFsType2());
        }
        else if(finStateType == FinStateType.UNSATISFACTORY) {
            return loanQualityCategoriesCache.get(loanQualityCategoryMatrixCache.get(x).getLoanQualityByFsType3());
        }

        return loanQualityCategoriesCache.get(loanQualityCategoryMatrixCache.get(x).getLoanQualityByFsType3());
    }
}
