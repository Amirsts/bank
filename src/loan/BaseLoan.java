package loan;

import person.Customer;

import java.time.LocalDate;

public abstract class   BaseLoan {
    protected double loanAmount;           // Loan amount
    protected int duration;// Repayment period
    protected int fDuration;
    protected double customerShare;        // Percentage
    protected int penaltyRate;          // Late penalty percentage
    protected Customer borrower;        // Borrower customer
    private boolean active = true;
    protected double totalAmount;
    private double paidAmount = 0;
    private LocalDate ldTime;
    private LocalDate nowPayTime;


    public BaseLoan(double loanAmount, int duration,
                    double customerShare, int penaltyRate,LocalDate ldTime, Customer borrower) {
        if (loanAmount <= 0 || duration <= 0)
            throw new IllegalArgumentException("Loan amount and duration must be positive.");
        if (borrower == null)
            throw new IllegalArgumentException("Borrower cannot be null.");

        this.loanAmount = loanAmount;
        this.duration = duration;
        this.fDuration = duration;
        this.customerShare = customerShare;
        this.penaltyRate = penaltyRate;
        this.ldTime = ldTime;
        this.nowPayTime = ldTime;
        this.borrower = borrower;

    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public int getfDuration() {
        return fDuration;
    }

    public Customer getBorrower() {
        return borrower;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getRemainingAmount(){
        return totalAmount - paidAmount;
    }

    // Calculate the total amount of the penalty in case of delay (monthly)
    public double calculatePenalty(int delayInMonths) {
        return (loanAmount * penaltyRate / 100) * (delayInMonths - 1);
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


    public void pay(double amount ,LocalDate datePay) {
        if (amount == installmentPerMonth()) {
            this.paidAmount += amount;
            System.out.println("amount: " + amount + "Toman was paid on the loan.");
            fDuration--;
            nowPayTime = datePay;
        }else {
            System.out.println("مبلغ واریزی معتبر نمی باشد");
        }
    }



    public void payInstallment() {
        if (!active) {
            System.out.println("Loan is already closed.");
            return;
        }

        double installment = installmentPerMonth();

        int monthsSinceStart = nowPayTime.getMonthValue() - ldTime.getMonthValue() +
                12 * (nowPayTime.getYear() - ldTime.getYear());

        ldTime = nowPayTime;

        if (monthsSinceStart >= 2) {
            double penalty = calculatePenalty(monthsSinceStart);
            totalAmount += penalty;

            System.out.println("Penalty of " + penalty + " Toman applied due to " + (monthsSinceStart - 1) + " months delay.");
        }

        if (paidAmount >= totalAmount) {
            closeLoan();
            System.out.println("Loan is fully paid and now closed.");
        }
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
