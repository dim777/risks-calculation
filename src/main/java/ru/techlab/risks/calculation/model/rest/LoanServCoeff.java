package ru.techlab.risks.calculation.model.rest;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by rb052775 on 05.10.2017.
 */
@Data
public class LoanServCoeff implements Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    private String type;
    private Integer id;
    private Integer lastDays;
    private Integer moreThanDays;
}
