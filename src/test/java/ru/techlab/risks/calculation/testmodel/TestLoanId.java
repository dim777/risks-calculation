package ru.techlab.risks.calculation.testmodel;

import java.io.Serializable;

/**
 * Created by rb052775 on 27.09.2017.
 */
public class TestLoanId implements Serializable{
    private static final long serialVersionUID = 3375159358757648792L;

    private String branch;
    private String loanAccountNumber;
    private String loanAccountSuffix;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getLoanAccountSuffix() {
        return loanAccountSuffix;
    }

    public void setLoanAccountSuffix(String loanAccountSuffix) {
        this.loanAccountSuffix = loanAccountSuffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestLoanId that = (TestLoanId) o;

        if (branch != null ? !branch.equals(that.branch) : that.branch != null) return false;
        if (loanAccountNumber != null ? !loanAccountNumber.equals(that.loanAccountNumber) : that.loanAccountNumber != null)
            return false;
        return loanAccountSuffix != null ? loanAccountSuffix.equals(that.loanAccountSuffix) : that.loanAccountSuffix == null;
    }

    @Override
    public int hashCode() {
        int result = branch != null ? branch.hashCode() : 0;
        result = 31 * result + (loanAccountNumber != null ? loanAccountNumber.hashCode() : 0);
        result = 31 * result + (loanAccountSuffix != null ? loanAccountSuffix.hashCode() : 0);
        return result;
    }
}
