package fixingSuggest;

import java.util.ArrayList;

public class Teller {
    private static ArrayList<String> box = new ArrayList<>();

    public static void newRequest(String request) {
        box.add(request);
    }

    public static void acceptRequest(int index) {
        String massage = box.get(index);
        Manager.newRequest(massage);
        box.remove(index);
    }

    public static void seeBox() {
        for (int i = 0; i < box.size(); i++) {
            System.out.println(i+1 + ": " + box.get(i));
        }
        if (box.isEmpty()) {
            System.out.println("The box is empty.");
        }
        System.out.println("----------");
    }
}
