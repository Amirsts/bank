package branch;

import person.Employee;
import person.Customer;
import loan.*;
import interfaces.RequestHandler;
import request.Request;
import request.RequestType;

public class BranchManager extends Employee implements RequestHandler {
    public BranchManager(String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , 90000000);
    }

    @Override
    public void handleRequest(Request request){
        System.out.println("The branch manager is reviewing the request: " + request);

        if (request.getType() == RequestType.LOAN_REQUEST){
           request.setStatus("approved");
           request.getSender().addLoan(new NormalLoan(300_000_00 , 12, request.getSender()));
            System.out.println("Loan approved and added to customer.");
            for (Customer c : getAssignedBranch().getCustomers()) {
                if (!c.getActiveLoans().isEmpty()) continue;

                BaseLoan approvedLoan = new NormalLoan(300_000_000, 12, c);
                c.addLoan(approvedLoan);
                System.out.println("✅ Loan added to customer: " + c.getFullName());
                break;
            }


        }else if (request.getType() == RequestType.CLOSE_ACCOUNT){
            System.out.println("Account closed after final approval");
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

