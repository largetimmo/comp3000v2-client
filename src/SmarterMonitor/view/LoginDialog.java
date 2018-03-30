package SmarterMonitor.view;

import SmarterMonitor.Main;
import SmarterMonitor.socket.Socket;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.json.JsonObject;

public class LoginDialog {
    private Stage loginStage;
    private MainWindow mainWindow;
    private Main main;
    private Socket socket;

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
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @FXML
    private void loginStep(){
        String key;
        String password;
        String information;
        key = keyField.getText();
        password = passwordField.getText();
        //TODO Call a object to get the token
        JSONObject message = new JSONObject();
        JSONObject user = new JSONObject();
        message.put("ACTION","LOGIN");
        message.put("Target","");
        user.put("UID",key);
        user.put("PWD",password);
        message.put("DATA",user);
        socket.sendMessage(message.toString());

    }
}
