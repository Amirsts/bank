package branch;

import person.Customer;
import person.Employee;
import account.Account;
import loan.BaseLoan;

public class Teller extends Employee{

    public Teller (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId ,40000000 );
    }

    @Override
    public void handleRequest(String request){
        System.out.println("Teller is reviewing the request:" + request);

        if (request.startsWith("close account:")){
            String accountNumber = request.split(":")[1];
            processCloseAccount(accountNumber);

        }else if (request.startsWith("loan request:")){
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

        String msg = "close account:" + accountNumber;
        if (getAssignedBranch().getAssistantManager() != null ){
            getAssignedBranch().getAssistantManager().receiveRequest(msg);
            System.out.println("The request to close the account was referred to the assistant manager.");
        }else if (getAssignedBranch().getBranchManager() != null){
            getAssignedBranch().getBranchManager().receiveRequest(msg);
            System.out.println("The request to close the account was referred to the branch manager.");
        }else {
            System.out.println("Referral not possible: There is no deputy or branch manager.");
        }

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
