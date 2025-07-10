package branch;

import account.Account;
import person.Customer;
import person.Employee;
import request.Request;
import request.RequestType;
import message.MessageBox;

public class AssistantManager extends Employee  {

    private MessageBox messageBox = new MessageBox();

    public AssistantManager(String firstName, String lastName, String birthDay, String nationalCode,
                            String address, String phoneNum, String employeeId , String passWord) {

        super(firstName, lastName, birthDay, nationalCode, address, phoneNum, employeeId, 700000 , passWord);


    }


    public MessageBox getMessageBox() {
        return messageBox;
    }


}
