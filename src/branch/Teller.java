package branch;

import person.Customer;
import person.Employee;
import account.Account;
import loan.BaseLoan;

public class Teller extends Employee{

    public Teller (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId , long salary){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , salary);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("Teller is reviewing the request:" + request);

        if (request.startsWith("close account:")){
            String accountNumber = request.split(":")[1];
            processCloseAccount(accountNumber);

        }else if (request.startsWith("loan request:")){
            String loanDetails = request.substring("loan request:".length());
            sendLoanRequestToAssistant(loanDetails);
        }else {
            System.out.println("Invalid request!!!");
        }

    }

    private void processCloseAccount(String accountNumber){
        System.out.println("Reviewing account closing request, account number:" + accountNumber);

        /* --- --- this part probably will be completed --- ---*/


        System.out.println("If the account is current without a loan, the request will be referred to the Manager or AssistantManager.");
    }

    private void sendLoanRequestToAssistant(String loanDetails){
        if(getAssignedBranch() == null || getAssignedBranch().getBranchNumber() == null){
            System.out.println("Error: Branch or branch assistant not specified !");
            return;
        }
        System.out.println("Loan application sent to the branch assistant...");
        //getAssignedBranch().grtAssistantManager().receiveRequest("Loan request:" + loanDetails);
    }
}
