package ru.techlab.risks.calculation.services.quality;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseDelay;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.model.delay.DelayType;
import ru.xegex.risks.libs.model.loan.LoanQuality;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.util.stream.Stream;

/**
 * Created by rb052775 on 27.09.2017.
 */
@Service
public class QualityServiceImpl implements QualityService{
    @Autowired
    private DelayService delayService;

    @Override
    public LoanQuality calculateLoanQuality(BaseLoan loan){
        LocalDateTime now = LocalDateTime.now();
        Stream<BaseDelay> delayStream;

        try {
            delayStream = delayService.getDelaysByLoan(loan);
        }
        catch (DelayNotFoundException ex){
            return LoanQuality.GOOD;
        }

        //long delayCountI = delayStream.filter(baseDelay -> baseDelay.getDelayType().equals(DelayType.I)).count();

        long countBad = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanQuality.BAD.getLastDays()) return true;
                    return false;
                })
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanQuality.BAD.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countBad > 0) return LoanQuality.BAD;

        long countMid = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanQuality.MID.getLastDays()) return true;
                    return false;
                })
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanQuality.MID.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countMid > 0) return LoanQuality.MID;

        long countGood = delayStream
                .filter(baseDelay -> {
                    if(DateTimeUtils.differenceInDays(baseDelay.getStartDelayDate(), now) > LoanQuality.GOOD.getLastDays()) return true;
                    return false;
                })
                .filter(baseDelay -> {
                    if(baseDelay.getEndDate() - baseDelay.getStartDate() > LoanQuality.GOOD.getMoreThanDays()) return true;
                    return false;
                })
                .count();

        if(countGood > 0) return LoanQuality.GOOD;

        return null;
    }
}
