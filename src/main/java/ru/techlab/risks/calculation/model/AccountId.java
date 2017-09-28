package ru.techlab.risks.calculation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by rb052775 on 28.09.2017.
 */
@Data
@NoArgsConstructor
public class AccountId implements Serializable {
    private String branch;
    private String loanAccountNumber;
    private String loanAccountSuffix;
}
