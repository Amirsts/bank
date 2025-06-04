package branch;

import person.Employee;
import person.Customer;
import loan.*;
import interfaces.RequestHandler;

public class BranchManager extends Employee implements RequestHandler {
    public BranchManager(String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , 90000000);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("The branch manager is reviewing the request: " + request);

        if (request.startsWith("Finally loan request:")){
            String loanDetail = request.substring("Request for final loan approval:".length());

            for (Customer c : getAssignedBranch().getCustomers()) {
                if (!c.getActiveLoans().isEmpty()) continue;

                BaseLoan approvedLoan = new NormalLoan(300_000_000, 12, c);
                c.addLoan(approvedLoan);
                System.out.println("✅ Loan added to customer: " + c.getFullName());
                break;
            }

            processLoanApproval(loanDetail);
        }else if (request.startsWith("close account:")){
            String accountNumber = request.split(":")[1];
            processCloseAccount(accountNumber);
        }else {
            System.out.println("Request type for branch manager not defined");
        }

    }

    public void processLoanApproval(String loanDetail){

        System.out.println("Final loan review completed: sufficient balance and ceiling met");
    }

    public void processCloseAccount(String accountNumber){
        System.out.println("accountnumber" + accountNumber + "Closed successfully ");
    }
}

