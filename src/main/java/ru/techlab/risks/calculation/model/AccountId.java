package ru.techlab.risks.calculation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Created by rb052775 on 28.09.2017.
 */
@Data
@AllArgsConstructor
public class AccountId implements Serializable {
    private static final long serialVersionUID = 3375159358757648792L;

    @NonNull
    private String branch;
    @NonNull
    private String loanAccountNumber;
    @NonNull
    private String loanAccountSuffix;
}
