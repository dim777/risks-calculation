package ru.techlab.risks.calculation.services.quality;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.repository.DelaysRepository;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.model.customer.FinState;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;
import ru.xegex.risks.libs.model.quality.LoanQualityCategoryMatrix;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 27.09.2017.
 */
@Service
public class QualityServiceImpl extends LoanQualityCategoryMatrix implements QualityService {
    @Autowired
    private DelayService delayService;

    /**
     * Calculate LoanServCoeff
     * @param loan
     * @param endOfDay
     * @return
     */
    @Override
    public LoanServCoeff calculateLoanServCoeff(BaseLoan loan, LocalDateTime endOfDay){
        List<BaseDelay> delaysList = null;
        try {
            delaysList = delayService.getDelaysByLoanForLastNDays(loan, endOfDay, LoanServCoeff.BAD.getLastDays());
        } catch (DelayNotFoundException e) {
            return LoanServCoeff.GOOD;
        }

        long countBad = delaysList
                .stream()
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanServCoeff.BAD.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanServCoeff.BAD;

        long countMid = delaysList
                .stream()
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanServCoeff.MID.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countMid > 0) return LoanServCoeff.MID;

        long countGood = delaysList
                .stream()
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanServCoeff.GOOD.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanServCoeff.GOOD;

        return LoanServCoeff.GOOD;
    }

    /**
     * Calculate LoanQualityCategory
     * @param finState
     * @param loanQuality
     * @return
     */
    @Override
    public LoanQualityCategory calculateLoanQualityCategory(FinState finState, LoanServCoeff loanQuality){
        int x = 0, y = 0;
        if(loanQuality.equals(LoanServCoeff.GOOD)) x = 0;
        else if(loanQuality.equals(LoanServCoeff.MID)) x = 1;
        else if(loanQuality.equals(LoanServCoeff.BAD)) x = 2;

        if(finState.equals(FinState.GOOD)) y = 0;
        else if(finState.equals(FinState.MIDDLE)) y = 1;
        else if(finState.equals(FinState.UNSATISFACTORY)) y = 2;

        return loanQualityCategoryMatrix[x][y];
    }
}
