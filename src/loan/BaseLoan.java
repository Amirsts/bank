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
        this.totalAmount = loanAmount;
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


    public void pay(double amount) {
        if (amount == installmentPerMonth()) {
            this.paidAmount += amount;
            System.out.println("amount: " + amount + "Toman was paid on the loan.");
            duration--;
        }else {
            System.out.println("مبلغ واریزی معتبر نمی باشد");
        }
    }



    public void payInstallment(LocalDate paymentDate) {
        if (!active) {
            System.out.println("Loan is already closed.");
            return;
        }

        double installment = installmentPerMonth();

        // محاسبه تعداد ماه‌های گذشته از تاریخ دریافت وام تا تاریخ پرداخت فعلی
        int monthsSinceStart = paymentDate.getMonthValue() - ldTime.getMonthValue() +
                12 * (paymentDate.getYear() - ldTime.getYear());

        // محاسبه تعداد اقساطی که باید تا این تاریخ پرداخت شده باشند
        int expectedInstallments = Math.min(monthsSinceStart, duration);

        // محاسبه تعداد اقساطی که واقعاً پرداخت شده‌اند
        int paidInstallments = (int) (paidAmount / installment);

        // بررسی تأخیر
        int delay = expectedInstallments - paidInstallments;
        if (delay >= 2) { // یعنی بیش از 60 روز تأخیر داشته
            double penalty = calculatePenalty(delay);
            totalAmount += penalty;

            System.out.println("Penalty of " + penalty + " Toman applied due to " + delay + " months delay.");
        }

        // بررسی اینکه آیا وام کامل پرداخت شده
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
