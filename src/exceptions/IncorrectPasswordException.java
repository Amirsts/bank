package exceptions;

public class IncorrectPasswordException extends Exception{
   public IncorrectPasswordException(String dw){
        super(dw);
    }
}
