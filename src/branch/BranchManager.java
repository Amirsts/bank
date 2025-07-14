package branch;

import account.Account;
import person.Employee;
import person.Customer;
import loan.NormalLoan;
import request.Request;
import request.RequestType;
import message.MessageBox;

import java.time.LocalDate;

public class BranchManager extends Employee  {


    public BranchManager(String firstName, String lastName, String birthDay, String nationalCode,
                         String address, String phoneNum, String employeeId , String passWord) {

        super(firstName, lastName, birthDay, nationalCode, address, phoneNum, employeeId, 80000000 ,passWord);

    }

}
