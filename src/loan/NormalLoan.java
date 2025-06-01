package loan;

import person.Customer;

public class NormalLoan extends BaseLoan {

    public NormalLoan(int loanAmount, int duration, Customer borrower) {
        super(loanAmount, duration, 77,23,6, borrower);
        // 77: governmentShare = 100 - 23
        // 23: customerShare
        // 6: penaltyRate
    }

    // Calculation of the total refund amount by the customer
    @Override
    public int calculateTotalRepayment() {
        // Only the customer's share will be refunded
        return loanAmount * customerShare / 100;
    }
}

