package exceptions;

import interfaces.Payable;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String dw){
        super(dw);
    }
}
