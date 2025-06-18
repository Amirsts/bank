package exceptions;

public class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException(String dw){
        super(dw);
    }
}
