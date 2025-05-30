import java.time.LocalDate;

public class Person {
    private String firstName ;
    private String lastName;
    private String birthDay ;
    private String nationalCode;
    private String adders;
    private String phoneNum;

    public Person(String firstName , String lastName , String birthDay , String nationalCode , String adders , String phoneNum){
        validphoneNum(phoneNum);
        validNationalCode(nationalCode);

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.nationalCode = nationalCode;
        this.adders = adders;
        this.phoneNum = phoneNum;
    }

    //validations
    private void validNationalCode (String nationalCode){
        if (nationalCode == null || !nationalCode.matches("\\d{10}")){
            throw new IllegalArgumentException("Warning: National Number should be 10 digit !");
        }
    }

    private void validphoneNum (String phoneNum){
        if (phoneNum == null || !phoneNum.matches("0\\d{10}")){
            throw new IllegalArgumentException("Warning: Phone number must be started with 0 & 10 digits after that !");
        }
    }

    //getters
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

    public String getAdders(){
        return adders;
    }

    public String getPhoneNum(){
        return phoneNum;
    }

    //setters
    public void setPhoneNum(String phoneNum){
        validphoneNum(phoneNum);
        this.phoneNum = phoneNum;
    }

    public void setAdders(String adders) {
        this.adders = adders;
    }

}
