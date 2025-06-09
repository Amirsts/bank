package exceptions;

public class InvalidAmountException extends Exception{
    public InvalidAmountException(String dw){
        super(dw);
    }
}
