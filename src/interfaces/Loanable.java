package interfaces;

public interface Loanable {
    boolean isEligibleForLoan();
    double getLoanCeiling();
}
