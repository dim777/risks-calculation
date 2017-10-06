package ru.techlab.risks.calculation.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.joda.time.LocalDateTime;
import ru.xegex.risks.libs.model.config.Config;

import java.io.Serializable;

/**
 * Created by rb052775 on 05.10.2017.
 */
@Data
public class BaseConfig implements Config, Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    private Integer id;
    private String endOfDay;

    @Override
    @JsonIgnore
    public LocalDateTime getJodaEndOfDay() {
        return LocalDateTime.parse(endOfDay);
    }
}
