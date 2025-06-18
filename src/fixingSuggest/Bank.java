package fixingSuggest;

import java.util.ArrayList;

public class Bank {
    private static ArrayList<Customer> customers = new ArrayList<>();

    public static void setCustomer(Customer customer) {
        Bank.customers.add(customer);
    }

    public static Customer contains(String name) {
        for (Customer c : customers) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }
}
