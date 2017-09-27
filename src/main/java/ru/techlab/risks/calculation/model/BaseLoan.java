package ru.techlab.risks.calculation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.model.account.Account;
import ru.xegex.risks.libs.model.loan.Loan;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by rb052775 on 21.08.2017.
 */
@Table("SDDU")
@Data
public class BaseLoan implements Loan, Serializable{
    private static final long serialVersionUID = 3375159358757648792L;

    //@PrimaryKeyColumn(name = "id", ordinal = 4, type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.PARTITIONED)
    private long id;
    /**
     * Отделение ссудного счета
     */
    //@PrimaryKeyColumn(name = "ddabd", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    @Column("ddabd")
    private String branch;

    /**
     * Баз. ном. ссудн. счета
     */
    //@PrimaryKeyColumn(name = "ddand", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    @Column("ddand")
    private String loanAccountNumber;

    /**
     * Суффикс ссудного счета
     */
    //@PrimaryKeyColumn(name = "ddasd", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @Column("ddasd")
    private String loanAccountSuffix;

    /**
     * Дата начала ссуды
     */
    //@PrimaryKeyColumn(name = "dddt1", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    @Column("dddt1")
    private double startDate;

    /**
     * DDAMN: Остаток ссудной задолженности: осн.долг и просрочка
     */
    @Column("ddamn")
    private double balance;

    @Column("ddact")
    private String active;

    private Optional<Account> account;

    @Override
    /**
     * TODO: business logic shoud be excluded from model
     * */
    public LocalDateTime getStartDate(){
        try {
            return DateTimeUtils.convertFromAs400Format(this.startDate);
        } catch (ConvertionEx convertionEx) {
            convertionEx.printStackTrace();
        }
        return new LocalDateTime(Long.MIN_VALUE);
    }

    @Override
    public Optional<Account> getAccount() {
        return account;
    }
}
