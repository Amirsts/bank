package request;

import person.Customer;
import java.time.LocalDateTime;

public class Request {

    private RequestType type;
    private String message;
    private Customer sender;
    private String status;
    private LocalDateTime timestamp;

    public Request(RequestType type , String message , Customer sender){
        this.type = type;
        this.message = message;
        this.sender = sender;
        this.status = "pending";
        this.timestamp = LocalDateTime.of(2025 , 05 , 15 , 12 , 30);
    }

    //getters  & setters
    public RequestType getType(){
        return type;
    }

    public String getMessage(){
        return message;
    }

    public Customer getSender(){
        return sender;
    }

    public String getStatus(){
        return status;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public void setStatus(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return "(" + type + ")" + "||| sender" + sender.getFullName() + "||| status" + status;
    }
}
