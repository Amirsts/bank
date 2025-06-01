package loan;

import person.Customer;

public abstract class BaseLoan {
    protected int loanAmount;           // Loan amount
    protected int duration;             // Repayment period
    protected int governmentShare;      // Percentage
    protected int customerShare;        // Percentage
    protected int penaltyRate;          // Late penalty percentage
    protected Customer borrower;        // Borrower customer

    public BaseLoan(int loanAmount, int duration, int governmentShare,
                    int customerShare, int penaltyRate, Customer borrower) {
        if (loanAmount <= 0 || duration <= 0)
            throw new IllegalArgumentException("Loan amount and duration must be positive.");
        if (governmentShare < 0 || governmentShare > 100 ||
                customerShare < 0 || customerShare > 100 ||
                penaltyRate < 0 || penaltyRate > 100)
            throw new IllegalArgumentException("Percentages must be between 0 and 100.");
        if (borrower == null)
            throw new IllegalArgumentException("Borrower cannot be null.");

        this.loanAmount = loanAmount;
        this.duration = duration;
        this.governmentShare = governmentShare;
        this.customerShare = customerShare;
        this.penaltyRate = penaltyRate;
        this.borrower = borrower;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getDuration() {
        return duration;
    }

    public Customer getBorrower() {
        return borrower;
    }

    // Calculate the total amount of the penalty in case of delay (monthly)
    public int calculatePenalty(int delayInMonths) {
        return (loanAmount * penaltyRate / 100) * delayInMonths;
    }

    // Abstract method to calculate the total refund amount
    public abstract int calculateTotalRepayment();

    @Override
    public String toString() {
        return "Loan{" +
                "amount=" + loanAmount +
                ", duration=" + duration +
                ", borrower=" + borrower.getFullName() +
                '}';
    }
}
