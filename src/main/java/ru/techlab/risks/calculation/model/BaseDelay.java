package ru.techlab.risks.calculation.model;

import lombok.Data;
import org.joda.time.LocalDateTime;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.model.delay.Delay;
import ru.xegex.risks.libs.model.delay.DelayType;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.io.Serializable;

/**
 * Created by rb052775 on 26.09.2017.
 */
@Table("SRRUSDDU")
@Data
public class BaseDelay implements Delay, Serializable {
    private static final long serialVersionUID = 3375159358757648792L;
    /**
     * Отделение ссудного счета DDABD
     */
    @PrimaryKeyColumn(name = "RRABD", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String branch;

    /**
     * Баз. ном. ссудн. счета
     */
    @PrimaryKeyColumn(name = "RRAND", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountNumber;

    /**
     * Суффикс ссудного счета
     */
    @PrimaryKeyColumn(name = "RRASD", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountSuffix;


    /**
     * Дата начала суды
     */
    @PrimaryKeyColumn(name = "RRDT1", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    private Double loanStartDate;

    /**
     * тип просрочки
     */
    @PrimaryKeyColumn(name = "RRIP", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
    private DelayType delayType;

    @Column("RRDTS")
    private Double startDate;

    @Column("RRDTE")
    private Double endDate;

    @Override
    public LocalDateTime getStartDelayDate() {
        try {
            return DateTimeUtils.convertFromAs400Format(this.startDate);
        } catch (ConvertionEx convertionEx) {
            convertionEx.printStackTrace();
        }
        return new LocalDateTime(Long.MIN_VALUE);
    }

    @Override
    public LocalDateTime getFinishDelayDate() {
        try {
            return DateTimeUtils.convertFromAs400Format(this.endDate);
        } catch (ConvertionEx convertionEx) {
            convertionEx.printStackTrace();
        }
        return new LocalDateTime(Long.MIN_VALUE);
    }

    @Override
    public DelayType getDelayType(){
        return delayType;
    }
}
