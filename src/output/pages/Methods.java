package output.pages;

import account.CurrentAccount;
import account.QarzAlHasanehAccount;
import account.ShortTermAccount;
import exceptions.IncorrectPasswordException;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidAmountException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import person.Customer;
import request.Request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Methods {


        /* ---   Teller Menu   --- */

         static VBox buttonRequestLoanTl(Request request) {
            VBox infoBox = new VBox(10);
            infoBox.getStyleClass().add("login-box");


            LoginPage.selectedTeller.clearMessageBox(request);
            SubMainPage.currentBranch.getAssistantManager().receiveRequest(request);
            request.setStatus("تحویلدار " + LoginPage.selectedTeller.getFullName() + ": درخواست وام شما به معاون شعبه ارسال شد");

            TextField reaction = new TextField("درخواست به معاون شعبه ارسال شد");
            reaction.setId("repayInfo");
            reaction.setAlignment(Pos.CENTER);
            reaction.setEditable(false);

            infoBox.getChildren().add(reaction);

            return infoBox;
        }


    static void buttonRegisterWithdraw(VBox infoBox , String accountNumber , String amount , String passWord) {

        CurrentAccount currentAccount = (CurrentAccount) SubMainPage.currentBranch.findAccount(accountNumber);
        if (currentAccount == null) {
            NewCustomer.showErrorAlert("شماره حساب وارد شده موجود نمی‌باشد");
        } else {
            try {
                currentAccount.secureWithdraw(Integer.parseInt(amount), passWord);
            } catch (IncorrectPasswordException | InvalidAmountException |
                     InsufficientBalanceException ex) {
                System.out.println("Error in collection: " + ex.getMessage());
            }

            TextField reaction = new TextField();
            reaction.setPromptText("مبلغ " + amount + "\nاز حساب " + currentAccount.getOwner().getFullName() + " برداشت شد");
            reaction.setId("password");
            reaction.setEditable(false);

            infoBox.getChildren().add(reaction);
        }
    }

    static void buttonRegisterDeposit(VBox infoBox , String accountNumber , String amount) {

        CurrentAccount currentAccount = (CurrentAccount) SubMainPage.currentBranch.findAccount(accountNumber);
        if (currentAccount == null) {
            NewCustomer.showErrorAlert("شماره حساب وارد شده موجود نمی‌باشد");
        } else {
            currentAccount.deposit(Integer.parseInt(amount));

            TextField reaction = new TextField();
            reaction.setPromptText("مبلغ " + amount + "\nبه حساب " + currentAccount.getOwner().getFullName() + " واریز شد");
            reaction.setId("password");
            reaction.setEditable(false);

            infoBox.getChildren().add(reaction);
        }
    }

    static VBox buttonRequestOp(Request request) {
        VBox infoBox = new VBox(10);
        infoBox.getStyleClass().add("login-box");


        LoginPage.selectedTeller.clearMessageBox(request);
        Customer senderRequest = request.getSender();

        TextField amount = new TextField();
        amount.setPromptText("مبلغ");
        amount.setId("amount");

        TextField date = new TextField();
        date.setPromptText("تاریخ را وارد کنید (01/01/1404)");
        date.setId("date");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز");
        passWord.setId("password");

        Button register = new Button("تایید درخواست");
        register.setId("loginButton");
        register.setPrefWidth(260);

        register.setOnAction(a -> {
            infoBox.getChildren().clear();
            switch (request.getMessage()){
                case "حساب جاری" :
                    String newAccountNumber1 = SubMainPage.bank.accountNumberCreator('1');
                    CurrentAccount newAccount1 = new CurrentAccount(newAccountNumber1 , senderRequest , Long.parseLong(amount.getText()) , passWord.getText());
                    SubMainPage.bank.addAccount(newAccount1);
                    SubMainPage.currentBranch.addAccount(newAccount1);
                    senderRequest.openAccount(newAccount1);
                    request.setStatus("مشتری گرامی: " + senderRequest.getFullName() +" || حساب جاری شما با رمز: " + passWord.getText() + " || شماره حساب: " + newAccountNumber1 + " افتتاح شد");

                    infoBox.getChildren().add(reaction("حساب جاری " + senderRequest.getFullName() + " افتتاح شد"));

                    break;

                case "حساب کوتاه مدت" :
                    String newAccountNumber2 = SubMainPage.bank.accountNumberCreator('2');

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateOpenAccount = LocalDate.parse(date.getText() , formatter);

                    ShortTermAccount newAccount2 = new ShortTermAccount(newAccountNumber2 , senderRequest , Long.parseLong(amount.getText()) , passWord.getText() , dateOpenAccount);
                    SubMainPage.bank.addAccount(newAccount2);
                    SubMainPage.currentBranch.addAccount(newAccount2);
                    senderRequest.openAccount(newAccount2);
                    senderRequest.openShortTermAccount(newAccount2);
                    request.setStatus("مشتری گرامی: " + senderRequest.getFullName() +" || حساب جاری شما با رمز: " + passWord.getText() + " || شماره حساب: " + newAccountNumber2 + " افتتاح شد");

                    infoBox.getChildren().add(reaction("حساب کوتاه مدت " + senderRequest.getFullName() + " افتتاح شد"));
                    break;

                case "حساب قرض الحسنه" :
                    String newAccountNumber3 = SubMainPage.bank.accountNumberCreator('3');
                    QarzAlHasanehAccount newAccount3 = new QarzAlHasanehAccount(newAccountNumber3 , senderRequest , Long.parseLong(amount.getText()) , passWord.getText());
                    SubMainPage.bank.addAccount(newAccount3);
                    SubMainPage.currentBranch.addAccount(newAccount3);
                    senderRequest.openAccount(newAccount3);
                    request.setStatus("مشتری گرامی: " + senderRequest.getFullName() +" || حساب قرض الحسنه شما با رمز: " + passWord.getText() + " || شماره حساب: " + newAccountNumber3 + " افتتاح شد");

                    infoBox.getChildren().add(reaction("حساب قرض الحسنه " + senderRequest.getFullName() + " افتتاح شد"));
                    break;
            }
        });

        infoBox.getChildren().addAll(amount, date, passWord , register);
        return infoBox;
    }

    static VBox buttonRequestCl(Request request ) {
        VBox infoBox = new VBox(10);
        infoBox.getStyleClass().add("login-box");


        LoginPage.selectedTeller.clearMessageBox(request);
        String accountNumber = request.getMessage();
        Customer senderRequest = request.getSender();

        if (senderRequest.hasActiveLoan()) {
            infoBox.getChildren().add(reaction("مشتری دارای وام فعال است"));
            request.setStatus(" تحویلدار: " + LoginPage.selectedTeller.getFullName() + " ||مشتری گرامی، شما یک وام فعال دارید. حساب شما قابل بستن نیست.");
            LoginPage.selectedTeller.clearMessageBox(request);
        }else {
            infoBox.getChildren().add(reaction("حساب مورد نظر با موفقیت بسته شد"));
            senderRequest.removeAccount(accountNumber);
            SubMainPage.currentBranch.removeAccount(accountNumber);
            SubMainPage.bank.removeAccount(accountNumber);
            request.setStatus(" تحویلدار: " + LoginPage.selectedTeller.getFullName() + " ||مشتری گرامی،حساب " + accountNumber + "بسته شد");
        }

        return infoBox;
    }

    static TextField reaction (String input) {
        TextField reaction = new TextField(input);
        reaction.setId("repayInfo");
        reaction.setAlignment(Pos.CENTER);
        reaction.setEditable(false);

        return reaction;
    }





    /* ---   Assistant Menu   --- */
    static VBox buttonRequestLoanAs(Request request) {
        VBox infoBox = new VBox(10);
        infoBox.getStyleClass().add("login-box");

        LoginPage.selectedAssistant.clearMessageBox(request);
        
        if (! LoginPage.selectedAssistant.isEligibleForLoan(request.getSender()) ) {
            infoBox.getChildren().add(reaction("مشتری دارای وام فعال است"));
        }else {
            LoginPage.selectedAssistant.clearMessageBox(request);
            SubMainPage.currentBranch.getAssistantManager().receiveRequest(request);
            request.setStatus("معاون شعبه " + LoginPage.selectedAssistant.getFullName() + ": درخواست وام شما به رئیس شعبه ارسال شد");

            infoBox.getChildren().add(reaction("درخواست به رئیس شعبه ارسال شد"));
        }

        return infoBox;
    }
}
