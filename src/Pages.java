package fixingSuggest;

import java.util.Scanner;

public class Pages {
    private static Scanner scanner = new Scanner(System.in);

    public static void firstPage() {
        System.out.println("""
                <<MENU>>
                1. login
                2. sign up
                3. teller page
                4. manager page""");

        System.out.print("--> ");
        String entry = scanner.next();
        int choice = Integer.parseInt(entry);

        switch (choice) {
            case 1 -> loginPage();
            case 2 -> signUpPage();
            case 3 -> tellerPage();
            case 4 -> managerPage();
            default -> {
                System.out.println("Invalid input! Try again...");
                firstPage();
            }
        }
    }

    public static void signUpPage() {
        scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        System.out.print("--> ");
        String name = scanner.next();
        if (Bank.contains(name)!=null) {
            System.out.println("You signed up before. Going to login page...");
            loginPage();
        } else {
            Customer newC = new Customer();
            newC.name = name;

            scanner = new Scanner(System.in);
            System.out.println("Enter your password:");
            System.out.print("--> ");
            int pass = scanner.nextInt();
            newC.pass = pass;

            Bank.setCustomer(newC);
            Teller.newRequest(newC.name + " is waiting for creating an account.");

            System.out.println("Your information's are seted successfully!");
            firstPage();
        }
    }

    public static void loginPage() {
        scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        System.out.print("--> ");
        String name = scanner.next();

        if (Bank.contains(name)!=null) {
            Customer c = Bank.contains(name);
            scanner = new Scanner(System.in);
            System.out.println("Enter your password:");
            System.out.print("--> ");
            int pass = scanner.nextInt();
            if (pass==c.pass) {
                customerPage(c);
            } else {
                System.out.println("Something went wrong! Trying again...");
                loginPage();
            }
        } else {
            System.out.println("Something went wrong! Trying again...");
            firstPage();
        }
    }

    public static void customerPage(Customer c) {
        if (c.createFlag) {
            System.out.println("It's the end!!!");
        } else {
            System.out.println("You have to wait for teller or manager to accept!");
            firstPage();
        }
    }

    public static void tellerPage() {
        scanner = new Scanner(System.in);
        System.out.println("""
                1. see massage box
                2. accept a request
                3. main menu""");
        System.out.print("--> ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
                Teller.seeBox();
                tellerPage();
            }
            case 2 -> {
                scanner = new Scanner(System.in);
                System.out.println("Which one do you want to access?");
                System.out.print("--> ");
                int index = scanner.nextInt() -1;
                Teller.acceptRequest(index);
                tellerPage();
            }
            case 3 -> firstPage();
            default -> {
                System.out.println("Something went wrong! Trying again...");
                firstPage();
            }
        }
    }

    public static void managerPage() {
        scanner = new Scanner(System.in);
        System.out.println("""
                1. see massage box
                2. accept a request
                3. main menu""");
        System.out.print("--> ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
                Manager.seeBox();
                managerPage();
            }
            case 2 -> {
                scanner = new Scanner(System.in);
                System.out.println("Which one do you want to access?");
                System.out.print("--> ");
                int index = scanner.nextInt() -1;
                Manager.acceptRequest(index);
                managerPage();
            }
            case 3 -> firstPage();
            default -> {
                System.out.println("Something went wrong! Trying again...");
                firstPage();
            }
        }
    }

}
