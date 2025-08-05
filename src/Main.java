import javafx.application.Application;
import javafx.stage.Stage;
import output.*;
import output.pages.*;

public class Main extends Application {
    public static void main(String[] args) {
        /* Making this part of project is suggested by ROBIN (Mobin Rangsaz)
           to have more orderly Main class

                      This project was created with passion
                                     by:
                                     AMIRMOHAMMAD & POURIA



                                                                            */
    //   MainPage.firstPage();
         SubMainPage.subMainPage();
         launch(args);

    }

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);

        SceneManager.addScene("login", LoginPage::getLoginScene);
        SceneManager.addScene("customerMenu", CustomerMenu::getCustomerMenu);
        SceneManager.addScene("creatingNewAccount", CustomerMenu::creatingNewAccount);
        SceneManager.addScene("transfer", CustomerMenu::transfer);
        SceneManager.addScene("loanRequest", CustomerMenu::loanRequest);
        SceneManager.addScene("loanRepayInfo", CustomerMenu::loanRepayInfo);
        SceneManager.addScene("loanRepayment" , CustomerMenu::loanRepayment);
        SceneManager.addScene("messageDisplay", CustomerMenu::messageDisplay);
        SceneManager.addScene("accountsBalance", CustomerMenu::accountsBalance);
        SceneManager.addScene("closeAccount", CustomerMenu::closeAccount);

        SceneManager.switchTo("login");
    }

}