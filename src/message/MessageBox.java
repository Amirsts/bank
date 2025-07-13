package message;

import request.Request;
import request.RequestType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageBox {

    private List<Request> requests;

    public MessageBox() {
        this.requests = new ArrayList<>();
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void removeRequest(Request request) {
        requests.remove(request);
    }

    public List<Request> getAllRequests() {
        return new ArrayList<>(requests);
    }

    public List<Request> getPendingRequests() {
        return requests.stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("pending"))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestsByType(RequestType type) {
        return requests.stream()
                .filter(r -> r.getType() == type)
                .collect(Collectors.toList());
    }

    public void printAll() {
        if (requests.isEmpty()) {
            System.out.println("No messages.");
        } else {
            for (Request r : requests) {
                System.out.println(r);
            }
        }
    }


    public int size() {
        return requests.size();
    }
}
