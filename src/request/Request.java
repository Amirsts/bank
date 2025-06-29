package request;

import person.Customer;
import java.time.LocalDateTime;

public class Request {

    private RequestType type;
    private String message;
    private Customer sender;
    private String status;
    private LocalDateTime timestamp;
    private String accountNumber;
    private String currentLevel;


    public Request(RequestType type, Customer sender, String message, String accountNumber) {
        this.type = type;
        this.message = message;
        this.sender = sender;
        this.accountNumber = accountNumber;
        this.status = "pending";
        this.timestamp = LocalDateTime.now();
    }

    public Request(RequestType type, String message, Customer sender) {
        this(type,sender , message, null);
    }

    // getters
    public RequestType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Customer getSender() {
        return sender;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "(" + type + ") ||| sender: " + sender.getFullName() + " ||| status: " + status;
    }

    public void setCurrentLevel(String manager) {
        this.currentLevel = manager;
    }

}
