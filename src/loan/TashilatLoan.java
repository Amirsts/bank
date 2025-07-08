package loan;

import person.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TashilatLoan extends BaseLoan {

    public TashilatLoan(double loanAmount, int duration,LocalDate ldTime, Customer borrower) {
        super(loanAmount, duration,0.334,8,ldTime,borrower);
        this.totalAmount = calculateTotalRepayment();
        // 96: governmentShare = 100 - 4
        // 4: customerShare
        // 8: penaltyRate
    }

    @Override
    public double calculateTotalRepayment() {
        return ( ((getRemainingAmount()) * customerShare *duration) / 100) + loanAmount ;
    }

    @Override
    public double installmentPerMonth(){
        return calculateTotalRepayment() / duration;
    }
}
