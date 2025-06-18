package branch;

import account.Account;
import person.Customer;
import person.Employee;
import interfaces.RequestHandler;
import request.Request;
import request.RequestType;
import message.MessageBox;

public class AssistantManager extends Employee implements RequestHandler {

    private MessageBox messageBox = new MessageBox();

    public AssistantManager(String firstName, String lastName, String birthDay, String nationalCode,
                            String address, String phoneNum, String employeeId) {

        super(firstName, lastName, birthDay, nationalCode, address, phoneNum, employeeId, 700000);
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    @Override
    public void handleRequest(Request request) {
        System.out.println("🧑‍💼 The branch assistant is reviewing the request:\n" + request);

        if (request.getType() == RequestType.LOAN_REQUEST) {
            if (!request.getSender().isEligibleForLoan()) {
                System.out.println("❌ Request rejected: Customer has an active loan.");
                request.setStatus("rejected");
                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.LOAN_REQUEST, "❌ Your loan request was rejected by the assistant manager (active loan exists).", request.getSender())
                );
            } else {
                System.out.println("✅ The request was approved and referred to the branch manager.");
                request.setStatus("approved by assistant");
                request.setCurrentLevel("manager");
                getAssignedBranch().getBranchManager().getMessageBox().addRequest(request);

                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.LOAN_REQUEST, "✅ Your loan request was approved by the assistant manager and sent to the manager.", request.getSender())
                );
            }
        }

        else if (request.getType() == RequestType.CLOSE_ACCOUNT) {
            String accountNumber = request.getAccountNumber();

            if (accountNumber == null || accountNumber.isEmpty()) {
                System.out.println("⚠️ Account number is missing or invalid.");
                request.setStatus("rejected - invalid account number");

                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.CLOSE_ACCOUNT, "❌ Your account closure request was rejected by the assistant manager (invalid account number).", request.getSender())
                );

                messageBox.removeRequest(request);
                return;
            }

            Account account = getAssignedBranch().findAccount(accountNumber);

            if (account == null || account.getOwner() == null || !account.getOwner().equals(request.getSender())) {
                System.out.println("❌ Account is invalid or doesn't belong to the customer.");
                request.setStatus("rejected");

                request.getSender().getMessageBox().addRequest(
                        new Request(RequestType.CLOSE_ACCOUNT, "❌ Your account closure request was rejected by the assistant manager (ownership issue).", request.getSender())
                );

                messageBox.removeRequest(request);
                return;
            }

            System.out.println("✅ The account closure request was approved and referred to the branch manager.");
            request.setStatus("approved by assistant");
            request.setCurrentLevel("manager");
            getAssignedBranch().getBranchManager().getMessageBox().addRequest(request);

            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.CLOSE_ACCOUNT, "✅ Your request to close the account was approved by the assistant manager and sent to the manager.", request.getSender())
            );
        }

        else {
            System.out.println("❓ The request type is unknown.");
            request.setStatus("rejected");

            request.getSender().getMessageBox().addRequest(
                    new Request(RequestType.OTHER, "❌ Your request was rejected by the assistant manager (unknown type).", request.getSender())
            );
        }

        messageBox.removeRequest(request);
    }
}
