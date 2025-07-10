package branch;

import person.Customer;
import person.Employee;
import account.Account;
import request.Request;
import request.RequestType;
import message.MessageBox;

public class Teller extends Employee {

    private MessageBox messageBox = new MessageBox();


    public Teller(String firstName, String lastName, String birthDay, String nationalCode,
                  String address, String phoneNum, String employeeId,String passWord) {
        super(firstName, lastName, birthDay, nationalCode, address, phoneNum, employeeId, 40000000 ,passWord);
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }




}
