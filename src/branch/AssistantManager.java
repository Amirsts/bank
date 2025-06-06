package branch;

import account.Account;
import person.Customer;
import person.Employee;
import interfaces.RequestHandler;
import request.Request;
import request.RequestType;


public class AssistantManager extends Employee implements RequestHandler     {

    public AssistantManager (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , 700000);
    }

    @Override
    public void handleRequest(Request request) {
        System.out.println("The branch assistant is reviewing the request:\n" + request);

        if (request.getType() == RequestType.LOAN_REQUEST) {
            if (!request.getSender().isEligibleForLoan()) {
                System.out.println("Request rejected: Customer has an active loan");
                request.setStatus("rejected");
            } else {
                System.out.println("The request was approved and referred to the branch manager.");
                request.setStatus("approved by assistant");
                getAssignedBranch().getBranchManager().receiveRequest(request);
            }
        }

        else if (request.getType() == RequestType.CLOSE_ACCOUNT) {
            String accountNumber = request.getAccountNumber();

            if (accountNumber == null || accountNumber.isEmpty()) {
                System.out.println("Account number is missing or invalid");
                request.setStatus("rejected - invalid account number");
                return;
            }

            Account account = getAssignedBranch().findAccount(accountNumber);

            if (account == null || account.getOwner() == null || !account.getOwner().equals(request.getSender())) {
                System.out.println("Account is not valid or doesn't belong to the customer.");
                request.setStatus("rejected");
                return;
            }

            System.out.println("The account closure request was approved and referred to the branch manager.");
            request.setStatus("approved by assistant");
            getAssignedBranch().getBranchManager().receiveRequest(request);
        }

        else {
            System.out.println("The request type is unknown.");
            request.setStatus("rejected");
        }
    }

}
