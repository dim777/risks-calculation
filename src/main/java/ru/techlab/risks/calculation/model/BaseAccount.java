package ru.techlab.risks.calculation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.model.account.Account;

import java.io.Serializable;

/**
 * Created by rb052775 on 22.08.2017.
 */
@Table("SCPF")
@Data
public class BaseAccount implements Account, Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    //@PrimaryKeyColumn(name = "id", ordinal = 4, type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.PARTITIONED)
    private Long id;
    /**
     * Отделение счета SCAB
     */
    //@PrimaryKeyColumn(name = "scab", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    @Column("scab")
    private String branch;

    /**
     * Баз. ном. счета SCAN
     */
    //@PrimaryKeyColumn(name = "scan", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    @Column("scan")
    private String accountNumber;

    /**
     * Суффикс ссудного счета
     */
    //@PrimaryKeyColumn(name = "scas", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @Column("scas")
    private String accountSuffix;

    @Override
    public String getAbdAndAsd() {
        return null;
    }
}
