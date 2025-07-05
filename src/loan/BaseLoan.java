package loan;

import person.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class   BaseLoan {
    protected double loanAmount;           // Loan amount
    protected int duration;             // Repayment period
    protected double customerShare;        // Percentage
    protected int penaltyRate;          // Late penalty percentage
    protected Customer borrower;        // Borrower customer
    private boolean active = true;
    protected double totalAmount;
    private double paidAmount = 0;
    private LocalDate ldTime;


    public BaseLoan(double loanAmount, int duration,
                    double customerShare, int penaltyRate,LocalDate ldTime, Customer borrower) {
        if (loanAmount <= 0 || duration <= 0)
            throw new IllegalArgumentException("Loan amount and duration must be positive.");
        if (borrower == null)
            throw new IllegalArgumentException("Borrower cannot be null.");

        this.loanAmount = loanAmount;
        this.duration = duration;
        this.customerShare = customerShare;
        this.penaltyRate = penaltyRate;
        this.ldTime = ldTime;
        this.borrower = borrower;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public int getDuration() {
        return duration;
    }

    public Customer getBorrower() {
        return borrower;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getTotalAmount (){
        return totalAmount;
    }
    public double getRemainingAmount(){
        return totalAmount - paidAmount;
    }

    // Calculate the total amount of the penalty in case of delay (monthly)
    public double calculatePenalty(int delayInMonths) {
        return (loanAmount * penaltyRate / 100) * delayInMonths;
    }

    // Abstract method to calculate the total refund amount
    public abstract double calculateTotalRepayment();

    public abstract  double installmentPerMonth();

    public boolean isActive() {
        return active;
    }

    public void closeLoan() {
        this.active = false;
    }
    public void pay(double amount){
        this.paidAmount += amount;
        System.out.println("amount: " + amount + "Toman was paid on the loan.");

    }

    @Override
    public String toString() {
        return "\nLoan{" +
                "amount=" + loanAmount +
                ", duration=" + duration +
                ", borrower=" + borrower.getFullName() +
                '}';
    }
}
