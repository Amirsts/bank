package branch;

import account.Account;
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
    public void handleRequest(Request request) {
        System.out.println("👨‍💼 The branch manager is reviewing the request:\n" + request);

        if (request.getType() == RequestType.LOAN_REQUEST) {
            request.setStatus("approved");
            request.getSender().addLoan(new NormalLoan(300_000_000, 12, request.getSender()));
            System.out.println("✅ Loan approved and added to customer.");
        }

        else if (request.getType() == RequestType.CLOSE_ACCOUNT) {
            String accountNumber = request.getAccountNumber();

            if (accountNumber == null || accountNumber.isEmpty()) {
                System.out.println("⚠️ Invalid account number.");
                request.setStatus("rejected - invalid format");
                return;
            }

            Account account = getAssignedBranch().findAccount(accountNumber);
            if (account == null) {
                System.out.println("❌ Account not found.");
                request.setStatus("rejected - account not found");
                return;
            }

            Customer owner = account.getOwner();
            if (owner == null || !owner.equals(request.getSender())) {
                System.out.println("❌ The account does not belong to this customer.");
                request.setStatus("rejected - not owner");
                return;
            }

            owner.removeAccount(accountNumber);
            getAssignedBranch().removeAccount(accountNumber);

            System.out.println("✅ Account closed successfully.");
            request.setStatus("account closed");
        }

        else {
            System.out.println("❓ Unknown request type.");
            request.setStatus("rejected");
        }
    }

}

