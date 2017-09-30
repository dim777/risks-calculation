package ru.techlab.risks.calculation.model;

import lombok.Data;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.customer.Customer;
import ru.xegex.risks.libs.model.customer.FinState;

import java.io.Serializable;

/**
 * Created by rb052775 on 30.09.2017.
 */
@Table("GFPG")
@Data
public class BaseCustomer implements Customer, Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    @PrimaryKeyColumn(name = "GFCUS", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @PrimaryKeyColumn(name = "GFC3R", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String financialState;

    @Column("GFCUN")
    private String name;

    @Override
    public FinState getFinState() throws QualityConvertionEx{
        Integer fs = Integer.parseInt(this.financialState);
        if(fs.equals(0) || fs.equals(1)) return FinState.GOOD;
        else if(fs.equals(2)) return FinState.MIDDLE;
        else if(fs.equals(3) || fs.equals(4) || fs.equals(5)) return FinState.UNSATISFACTORY;
        else throw new QualityConvertionEx("Couldn't convert Integer to FinState");
    }
}
