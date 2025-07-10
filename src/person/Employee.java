package person;

import branch.Branch;
import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidNationalCodeException;
import interfaces.Displayable;
import interfaces.IsPassWordTrue;
import message.MessageBox;
import request.*;
import message.MessageBox;

public abstract class Employee extends Person implements Displayable , IsPassWordTrue {
    private String employeeId;
    private long salary;
    private Branch assignedBranch;
    private MessageBox messageBox;
    private String passWord;


    public Employee (String firstName , String lastName , String birthDay , String nationalCode
            , String address , String phoneNum , String employeeId , long salary , String passWord){

        super(firstName , lastName , birthDay , nationalCode , address , phoneNum );

        this.employeeId = employeeId ;
        this.salary = salary;
        this.messageBox = new MessageBox();
        this.passWord = passWord;

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

    public MessageBox getMessageBox() {
        return messageBox;
    }


    public void  setAssignedBranch(Branch branch){
        this.assignedBranch = branch ;
    }

    //methods
    public void receiveRequest(Request request){
        messageBox.addRequest(request);
    }

    public void clearMessageBox(Request request){
        messageBox.removeRequest(request);
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
    @Override
    public boolean isPassWordTrue(String inpPassWord){
        if (this.passWord.equals(inpPassWord)){
            return true;
        }
        return false;
    }
}
