package ru.techlab.risks.calculation.model;

import lombok.Data;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by dim777 on 05.10.17.
 */
@Table("kks_results")
@Data
public class LoanQualityResult implements Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    @PrimaryKeyColumn(name = "branch_id", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private String branch;

    @PrimaryKeyColumn(name = "customer_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountNumber;

    @PrimaryKeyColumn(name = "suffix", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountSuffix;

    @Column("customer_name")
    private String customerName;

    @Column("dddt1")
    private double startDate;

    @Column("balance")
    private BigDecimal balance;

    @Column("kks0")
    private String loanQualityCategory;

    @Column("kks1")
    private String loanQualityCategoryForAllCustomerLoans;

    @Column("interest_rate")
    private Double interestRate;
}
