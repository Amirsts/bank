package loan;

import person.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NormalLoan extends BaseLoan {

    public NormalLoan(double loanAmount, int duration, LocalDate ldTime , Customer borrower) {
        super(loanAmount, duration, 1.917,6,ldTime , borrower);
        this.totalAmount = calculateTotalRepayment();
        // 77: governmentShare = 100 - 23
        // 23: customerShare
        // 6: penaltyRate
    }


    // Calculation of the total refund amount by the customer
    @Override
    public double calculateTotalRepayment() {
        // Only the customer's share will be refunded
        return ( (loanAmount * customerShare *duration) / 100) + loanAmount ;
    }

    @Override
    public double installmentPerMonth() {
        return getRemainingAmount() / fDuration;
    }
}

