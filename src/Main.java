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
        SceneManager.addScene("customerMenu", MenuCustomer::getCustomerMenu);
        SceneManager.addScene("creatingNewAccount", MenuCustomer::creatingNewAccount);
        SceneManager.addScene("transfer", MenuCustomer::transfer);
        SceneManager.addScene("loanRequest", MenuCustomer::loanRequest);
        SceneManager.addScene("loanRepayInfo", MenuCustomer::loanRepayInfo);
        SceneManager.addScene("loanRepayment" , MenuCustomer::loanRepayment);
        SceneManager.addScene("messageDisplay", MenuCustomer::messageDisplay);
        SceneManager.addScene("accountsBalance", MenuCustomer::accountsBalance);
        SceneManager.addScene("closeAccount", MenuCustomer::closeAccount);
        SceneManager.addScene("processNewCustomer", NewCustomer::processNewCustomer);
        SceneManager.addScene("getTellerMenu", MenuTeller::getTellerMenu);
        SceneManager.addScene("depositWithdraw", MenuTeller::depositWithdraw);
        SceneManager.addScene("forwardLoan", MenuTeller::forwardLoan);
        SceneManager.addScene("openAccountRequest", MenuTeller::openAccountRequest);
        SceneManager.addScene("closeAccountRequest", MenuTeller::closeAccountRequest);
        SceneManager.addScene("getAssistantMenu", MenuAssistantManager::getAssistantMenu);
        SceneManager.addScene("forwardLoanAs", MenuAssistantManager::forwardLoanAs);

        SceneManager.switchTo("login");
    }

}