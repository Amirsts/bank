package branch;

import person.Customer;
import person.Employee;
import account.Account;
import interfaces.RequestHandler;
import request.Request;
import request.RequestType;
import message.MessageBox;

public class Teller extends Employee implements RequestHandler {

    private MessageBox messageBox = new MessageBox();
    private String passWord;

    public Teller(String firstName, String lastName, String birthDay, String nationalCode,
                  String address, String phoneNum, String employeeId,String passWord) {
        super(firstName, lastName, birthDay, nationalCode, address, phoneNum, employeeId, 40000000);
        this.passWord = passWord;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    @Override
    public void handleRequest(Request request) {
        System.out.println("🏦 Teller is reviewing the request:\n" + request);

        if (request.getType() == RequestType.CLOSE_ACCOUNT) {
            processCloseAccount(request);
        } else if (request.getType() == RequestType.LOAN_REQUEST) {
            sendLoanRequestToAssistant(request);
        } else {
            System.out.println("❌ Invalid request type!");
            request.setStatus("rejected");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.OTHER, "❌ Your request was rejected by the teller (invalid type).", request.getSender())
            );
        }

        messageBox.removeRequest(request); // ✅ Always clean up after handling
    }

    private void processCloseAccount(Request request) {
        String accountNumber = request.getAccountNumber();
        System.out.println("📋 Reviewing account closing request, account number: " + accountNumber);

        if (getEmployeeId() == null) {
            System.out.println("❌ Error: The recipient is not assigned to any branch.");
            return;
        }

        Account account = getAssignedBranch().findAccount(accountNumber);
        if (account == null) {
            System.out.println("❌ Error: No account with this number found.");
            request.setStatus("rejected - account not found");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "❌ Account not found.", request.getSender())
            );
            return;
        }

        Customer owner = account.getOwner();
        if (owner == null) {
            System.out.println("❌ Account has no owner.");
            request.setStatus("rejected - no owner");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "❌ Account has no owner.", request.getSender())
            );
            return;
        }

        if (!owner.getActiveLoans().isEmpty()) {
            System.out.println("⛔ Cannot close the account: customer has active loan.");
            request.setStatus("rejected - active loan exists");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "⛔ Your request to close the account was rejected (active loan exists).", request.getSender())
            );
            return;
        }

        // Forwarding request
        if (getAssignedBranch().getAssistantManager() != null) {
            request.setStatus("forwarded to assistant");
            request.setCurrentLevel("assistant");
            getAssignedBranch().getAssistantManager().getMessageBox().addRequest(request);
            System.out.println("📨 The request to close the account was referred to the assistant manager.");

            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "✅ Your account closure request was forwarded to assistant manager.", request.getSender())
            );

        } else if (getAssignedBranch().getBranchManager() != null) {
            request.setStatus("forwarded to manager");
            request.setCurrentLevel("manager");
            getAssignedBranch().getBranchManager().getMessageBox().addRequest(request);
            System.out.println("📨 The request to close the account was referred to the branch manager.");

            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "✅ Your account closure request was forwarded to branch manager.", request.getSender())
            );
        } else {
            System.out.println("❌ Referral not possible: No assistant or branch manager found.");
            request.setStatus("rejected - no handler available");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "❌ Request failed: No available manager to handle the request.", request.getSender())
            );
        }
    }

    private void sendLoanRequestToAssistant(Request request) {
        if (getAssignedBranch() == null || getAssignedBranch().getBranchNumber() == null) {
            System.out.println("❌ Error: Branch or branch assistant not specified!");
            request.setStatus("rejected - no branch");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.LOAN_REQUEST, "❌ Loan request failed: no valid branch assigned.", request.getSender())
            );
            return;
        }

        request.setStatus("forwarded to assistant");
        request.setCurrentLevel("assistant");
        getAssignedBranch().getAssistantManager().getMessageBox().addRequest(request);

        request.getSender().getMessageBox().addRequest(
                new Request(RequestType.LOAN_REQUEST, "✅ Your loan request was forwarded to assistant manager.", request.getSender())
        );

        System.out.println("📨 Loan application sent to the branch assistant...");
    }

    public boolean isPassWordTrue(String tPass){
        if (passWord.equals(tPass)){
            return true;
        }
        return false;
    }

}
