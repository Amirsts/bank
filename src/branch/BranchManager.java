package branch;

import person.Employee;

public class BranchManager extends Employee {
    public BranchManager(String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , 90000000);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("The branch manager is reviewing the request: " + request);

        if (request.startsWith("Request for final loan approval:")){
            String loanDetail = request.substring("Request for final loan approval:".length());
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
