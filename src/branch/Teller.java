package branch;

import person.Customer;
import person.Employee;
import account.Account;
import interfaces.RequestHandler;
import request.Request;
import request.RequestType;

public class Teller extends Employee implements RequestHandler{

    public Teller (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId ,40000000 );
    }

    @Override
    public void handleRequest(Request request){
        System.out.println("Teller is reviewing the request:" + request);

        if (request.getType() == RequestType.CLOSE_ACCOUNT){
            String accountNumber = request.getAccountNumber();
            processCloseAccount(accountNumber);

        }else if (request.getType() == RequestType.LOAN_REQUEST){
            sendLoanRequestToAssistant(request);
        }else {
            System.out.println("Invalid request!!!");
        }

    }



    private void processCloseAccount(String accountNumber){
        System.out.println("Reviewing account closing request, account number:" + accountNumber);

        if (getEmployeeId() == null){
            System.out.println("Error: The recipient is not assigned to any branch.");
            return;
        }

        Account account = getAssignedBranch().findAccount(accountNumber);
        if (account == null){
            System.out.println("Error: No account with this number found!!!");
            return;
        }


        Customer owner = account.getOwner();
        if (owner == null){
            System.out.println("Account doesn't have owner ");
            return;
        }

        if (!owner.getActiveLoans().isEmpty()){
            System.out.println("It is not possible to close the account: the customer has an active loan.");
            return;
        }
            Request request = new Request(
                    RequestType.CLOSE_ACCOUNT , "Request to close account no" + accountNumber , owner ,accountNumber);

       // String msg = "close account:" + accountNumber;
        if (getAssignedBranch().getAssistantManager() != null ){
            getAssignedBranch().getAssistantManager().receiveRequest(request);
            System.out.println("The request to close the account was referred to the assistant manager.");
        }else if (getAssignedBranch().getBranchManager() != null){
            getAssignedBranch().getBranchManager().receiveRequest(request);
            System.out.println("The request to close the account was referred to the branch manager.");
        }else {
            System.out.println("Referral not possible: There is no deputy or branch manager.");
        }

    }



    private void sendLoanRequestToAssistant(Request request){
        if(getAssignedBranch() == null || getAssignedBranch().getBranchNumber() == null){
            System.out.println("Error: Branch or branch assistant not specified !");
            return;
        }
        System.out.println("Loan application sent to the branch assistant...");
        getAssignedBranch().getAssistantManager().receiveRequest(request);
    }
}
