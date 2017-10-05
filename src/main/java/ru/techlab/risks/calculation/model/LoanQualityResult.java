package ru.techlab.risks.calculation.model;

import lombok.Data;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.model.quality.LoanQualityCategory;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by dim777 on 05.10.17.
 */
@Table("KKS_RESULTS")
@Data
public class LoanQualityResult implements Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    @PrimaryKeyColumn(name = "branchId", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private String branch;

    @PrimaryKeyColumn(name = "customerId", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountNumber;

    @PrimaryKeyColumn(name = "suffix", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String loanAccountSuffix;

    @Column("customerName")
    private Optional<String> customerName;

    @Column("kks0")
    private Optional<LoanQualityCategory> loanQualityCategory;

    @Column("kks1")
    private Optional<LoanQualityCategory> loanQualityCategoryForAllCustomerLoans;

}
