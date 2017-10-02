package ru.techlab.risks.calculation.services.quality;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.model.customer.FinState;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;
import ru.xegex.risks.libs.model.quality.LoanQualityCategoryMatrix;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.util.stream.Stream;

/**
 * Created by rb052775 on 27.09.2017.
 */
@Service
public class QualityServiceImpl extends LoanQualityCategoryMatrix implements QualityService {
    @Autowired
    private DelayService delayService;

    @Override
    public LoanServCoeff calculateLoanServCoeff(BaseLoan loan){
        LocalDateTime now = LocalDateTime.now();
        Stream<BaseDelay> delayStream;

        try {
            delayStream = delayService.getDelaysByLoan(loan);
        }
        catch (DelayNotFoundException ex){
            return LoanServCoeff.GOOD;
        }

        //long delayCountI = delayStream.filter(baseDelay -> baseDelay.getDelayType().equals(DelayType.I)).count();

        long countBadTemp = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanServCoeff.BAD.getLastDays()) return true;
                    return false;
                })
                .count();

        long countBad = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanServCoeff.BAD.getLastDays()) return true;
                    return false;
                })
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanServCoeff.BAD.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanServCoeff.BAD;

        long countMid = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanServCoeff.MID.getLastDays()) return true;
                    return false;
                })
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanServCoeff.MID.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countMid > 0) return LoanServCoeff.MID;

        long countGood = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanServCoeff.GOOD.getLastDays()) return true;
                    return false;
                })
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanServCoeff.GOOD.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countGood > 0) return LoanServCoeff.GOOD;

        return null;
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
