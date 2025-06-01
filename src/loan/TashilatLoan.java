package loan;

import person.Customer;

public class TashilatLoan extends BaseLoan {

    public TashilatLoan(int loanAmount, int duration, Customer borrower) {
        super(loanAmount, duration,96,4,8, borrower);
        // 96: governmentShare = 100 - 4
        // 4: customerShare
        // 8: penaltyRate
    }

    @Override
    public int calculateTotalRepayment() {
        return loanAmount * customerShare / 100;
    }
}
