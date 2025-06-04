package person;

import branch.Branch;
import java.util.ArrayList;
import java.util.List;
import interfaces.Displayable;

public abstract class Employee extends Person implements Displayable{
    private String employeeId;
    private long salary;
    private Branch assignedBranch;
    private List<String> messageBox;


    public Employee (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId , long salary){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum );

        this.employeeId = employeeId ;
        this.salary = salary;
        this.messageBox = new ArrayList<>();
    }

    //getters & setters

    public String getEmployeeId(){
        return  employeeId;
    }

    public long getSalary() {
        return salary;
    }

    public Branch getAssignedBranch(){
        return assignedBranch;
    }

    public List<String> getMessageBox(){
        return messageBox;
    }

    public void setAssignedBranch(Branch branch1){
        this.assignedBranch = branch1 ;
    }

    //methods
    public void receiveRequest(String request){
        messageBox.add(request);
    }

    public void clearMessageBox(){
        messageBox.clear();
    }

    @Override

    public String toString(){
        return super.toString() +
                "Person.Employee Id: " +employeeId +
                "Monthly salary: "+ salary+
                "Workplace branch: "+ (assignedBranch != null ? assignedBranch.getBranchNumber() :"Not identified!");
    }

    @Override
    public void displayInfo(){

    }
}
