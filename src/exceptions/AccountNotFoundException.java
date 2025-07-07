package exceptions;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String dw){
        super(dw);
    }
}
