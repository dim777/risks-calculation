package ru.techlab.risks.calculation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.model.account.Account;
import ru.xegex.risks.libs.model.customer.Customer;
import ru.xegex.risks.libs.model.loan.Loan;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;
import ru.xegex.risks.libs.utils.DateTimeUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by rb052775 on 21.08.2017.
 */
@Table("SDDU")
@Data
public class BaseLoan implements Loan, Serializable{
    private static final long serialVersionUID = 3375159358757648792L;

    /**
     * Отделение ссудного счета
     */
    @PrimaryKeyColumn(name = "ddabd", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private String branch;

    /**
     * Баз. ном. ссудн. счета
     */
    @PrimaryKeyColumn(name = "ddand", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountNumber;

    /**
     * Суффикс ссудного счета
     */
    @PrimaryKeyColumn(name = "ddasd", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountSuffix;

    /**
     * Дата начала ссуды
     */
    @Column("dddt1")
    private double startDate;

    /**
     * DDAMN: Остаток ссудной задолженности: осн.долг и просрочка
     */
    @Column("ddamn")
    private BigDecimal balance;

    @Column("ddact")
    private String active;

    private Optional<BaseCustomer> baseCustomer;

    @Override
    public LocalDateTime getStartDate(){
        try {
            return DateTimeUtils.convertFromAs400Format(this.startDate);
        } catch (ConvertionEx convertionEx) {
            convertionEx.printStackTrace();
        }
        return new LocalDateTime(Long.MIN_VALUE);
    }

}
