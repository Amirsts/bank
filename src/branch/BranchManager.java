package branch;

import account.Account;
import person.Employee;
import person.Customer;
import loan.NormalLoan;
import interfaces.RequestHandler;
import request.Request;
import request.RequestType;
import message.MessageBox;

import java.time.LocalDate;

public class BranchManager extends Employee implements RequestHandler {

    private MessageBox messageBox = new MessageBox();

    public BranchManager(String firstName, String lastName, String birthDay, String nationalCode,
                         String address, String phoneNum, String employeeId) {

        super(firstName, lastName, birthDay, nationalCode, address, phoneNum, employeeId, 80000000);
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    @Override
    public void handleRequest(Request request) {
        System.out.println("👨‍💼 The branch manager is reviewing the request:\n" + request);

        if (request.getType() == RequestType.LOAN_REQUEST) {
            request.setStatus("approved");
            LocalDate date = LocalDate.now();
            request.getSender().addLoan(new NormalLoan(300_000_000, 12, date ,request.getSender()));
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.LOAN_REQUEST, "✅ Your loan has been approved by the branch manager.",
                            request.getSender()));
            System.out.println("✅ Loan approved and added to customer.");
        }

        else if (request.getType() == RequestType.CLOSE_ACCOUNT) {
            String accountNumber = request.getAccountNumber();

            if (accountNumber == null || accountNumber.isEmpty()) {
                System.out.println("⚠️ Invalid account number.");
                request.setStatus("rejected - invalid format");
                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.CLOSE_ACCOUNT, "❌ Invalid account number format.",
                                request.getSender()));
                messageBox.removeRequest(request);
                return;
            }

            Account account = getAssignedBranch().findAccount(accountNumber);
            if (account == null) {
                System.out.println("❌ Account not found.");
                request.setStatus("rejected - account not found");
                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.CLOSE_ACCOUNT, "❌ Account not found in branch.",
                                request.getSender()));
                messageBox.removeRequest(request);
                return;
            }

            Customer owner = account.getOwner();
            if (owner == null || !owner.equals(request.getSender())) {
                System.out.println("❌ The account does not belong to this customer.");
                request.setStatus("rejected - not owner");
                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.CLOSE_ACCOUNT, "❌ This account doesn't belong to you.",
                                request.getSender()));
                messageBox.removeRequest(request);
                return;
            }

            owner.removeAccount(accountNumber);
            getAssignedBranch().removeAccount(accountNumber);

            request.setStatus("account closed");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "✅ Your account has been closed by branch manager.",
                            request.getSender()));

            System.out.println("✅ Account closed successfully.");
        }

        else {
            System.out.println("❓ Unknown request type.");
            request.setStatus("rejected");
            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.OTHER, "❌ Unknown request type.", request.getSender()));
        }

        messageBox.removeRequest(request); // Remove after handling
    }
}
