package branch;

import person.Employee;

public class AssistantManager extends Employee {

    public AssistantManager (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum , employeeId , 700000);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("The branch assistant is reviewing the request:" + request);

        if (request.startsWith("loan request:")) {
            boolean hasActiveLoan = checkIfCustomerHasActiveLoan();

            if (hasActiveLoan) {
                System.out.println("Request rejected: Customer has an active loan");
            } else {
                System.out.println("The request was approved and referred to the branch manager.");

                if (getAssignedBranch() != null && getAssignedBranch().getBranchManager() != null) {
                    getAssignedBranch().getBranchManager().receiveRequest("Finally loan request: " + request);
                } else {
                    System.out.println("Error: Branch manager not defined");
                }

            }
        }else{
            System.out.println("The request type for the branch assistant is not defined.");
        }
    }

    private boolean checkIfCustomerHasActiveLoan(){
        /*this prat will be completed*/
        return false;
    }
}
