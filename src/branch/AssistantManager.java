package branch;

import person.Customer;
import person.Employee;
import interfaces.RequestHandler;

public class AssistantManager extends Employee implements RequestHandler     {

    public AssistantManager (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , 700000);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("The branch assistant is reviewing the request: \n" + request);

        if (request.startsWith("loan request:")) {

            boolean hasActiveLoan = checkIfCustomerHasActiveLoan();

            if (hasActiveLoan) {
                System.out.println("Request rejected: Customer has an active loan");
            } else {
                System.out.println("The request was approved and referred to the branch manager.");

                if (getAssignedBranch() != null && getAssignedBranch().getBranchManager() != null) {
                    getAssignedBranch().getBranchManager().receiveRequest("Finally loan request:" + request);
                } else {
                    System.out.println("Error: Branch manager not defined");
                }

            }
        }else{
            System.out.println("The request type for the branch assistant is not defined.");
        }
    }

    private boolean checkIfCustomerHasActiveLoan(){
        if(getAssignedBranch() == null) return false;
        for (Customer c : getAssignedBranch().getCustomers()){
            if (!c.getActiveLoans().isEmpty()){

                System.out.println("Customer: " + c.getFullName() + " have an active loan.");
                return true;
            }

        }
        return false;
    }
}
