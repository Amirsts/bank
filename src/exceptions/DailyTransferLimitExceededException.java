package exceptions;

public class DailyTransferLimitExceededException extends Exception {
    public DailyTransferLimitExceededException(String message) {
        super(message);
    }
}