package person;

import exceptions.InvalidNationalCodeException;
import exceptions.InvalidPhoneNumberException;

public class Person {
    private String firstName ;
    private String lastName;
    private String birthDay ;
    private String nationalCode;
    private String address;
    private String phoneNum;

    public Person(){}
    public Person(String firstName , String lastName , String birthDay , String nationalCode , String address , String phoneNum){
        validNationalCode(nationalCode);
        validphoneNum(phoneNum);

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.nationalCode = nationalCode;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    //validations
    private void validNationalCode (String nationalCode){
        if (nationalCode == null || !nationalCode.matches("\\d{10}")){
            throw new InvalidNationalCodeException("Warning: National Number should be 10 digit !");
        }
    }

    private void validphoneNum (String phoneNum){
        if (phoneNum == null || !phoneNum.matches("09\\d{9}")){
            throw new InvalidPhoneNumberException("Warning: Phone number must be started with 09 & 11 digits after that !");
        }
    }

    //getters
    public String getFullName(){return firstName + " " + lastName; }
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getNationalCode(){
        return nationalCode;
    }

    public String getAddress(){
        return address;
    }

    public String getPhoneNum(){
        return phoneNum;
    }

    //setters
    public void setPhoneNum(String phoneNum){
        validphoneNum(phoneNum);
        this.phoneNum = phoneNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //toString

    @Override
    public String toString(){
        return "Name: " +firstName +
                " "+ lastName+
                "\nNational Id: " + nationalCode +
                "\nBirthday: "+ birthDay +
                "\nphone number: "+ phoneNum +
                "\nAddress: "+ address;
    }

}
