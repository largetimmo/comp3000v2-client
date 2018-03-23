package SmarterMonitor.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginDialog {
    private Stage loginStage;
    private MainWindow mainWindow;

    @FXML
    private TextField keyField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void initialize() {
    }
    public void setLoginStage(Stage loginStage){
        this.loginStage = loginStage;
    }
    public void setMainWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @FXML
    private void loginStep(){
        String key;
        String password;
        key = keyField.getText();
        password = passwordField.getText();
    }
}
