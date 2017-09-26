package ru.techlab.risks.calculation.model;

import lombok.Data;
import org.joda.time.LocalDateTime;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.model.delay.Delay;
import ru.xegex.risks.libs.model.delay.DelayType;
import ru.xegex.risks.libs.utils.DateTimeUtils;

/**
 * Created by rb052775 on 26.09.2017.
 */
@Table("sddu")
@Data
public class SimpleDelay implements Delay {
    /**
     * Отделение ссудного счета DDABD
     */
    @Column("RRABD")
    private String branch;

    /**
     * Баз. ном. ссудн. счета
     */
    //@PrimaryKeyColumn(name = "ddand", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    @Column("RRAND")
    private String loanAccountNumber;

    /**
     * Суффикс ссудного счета
     */
    //@PrimaryKeyColumn(name = "ddasd", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @Column("RRASD")
    private String loanAccountSuffix;


    /**
     * Дата начала суды
     */
    @Column("RRDT1")
    private Double loanStartDate;

    /**
     * тип просрочки
     */
    @Column("RRIP")
    private DelayType delayType;

    @Column("RRDTS")
    private Double startDate;

    @Column("RPDTS")
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
}
