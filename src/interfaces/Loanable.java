package interfaces;

public interface Loanable {
    boolean isEligibleForLoan();
    double getNormalLoanCeiling();
    double getTashilatCeiling();
}
