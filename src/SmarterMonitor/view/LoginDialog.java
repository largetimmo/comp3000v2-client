package SmarterMonitor.view;

import SmarterMonitor.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginDialog {
    private Stage loginStage;
    private MainWindow mainWindow;
    private Main main;

    @FXML
    private TextField keyField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label message;

    @FXML
    private void initialize() {
    }
    public void setLoginStage(Stage loginStage){
        this.loginStage = loginStage;
    }
    public void setMainWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }
    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    private void loginStep(){
        String key;
        String password;
        String information;
        key = keyField.getText();
        password = passwordField.getText();
        //TODO Call a object to get the token
        if (true) { //TODO Change the true
            main.setToken("Token",key);
            loginStage.close();
        }
        else {
            message.setVisible(true);
        }
    }
}
