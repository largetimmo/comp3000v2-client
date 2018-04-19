package SmarterMonitor.view;

import SmarterMonitor.Main;
import SmarterMonitor.socket.SocketHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URISyntaxException;


public class ConnectWindow {
    @FXML
    TextField address;
    @FXML
    TextField port;
    @FXML
    Button connectServer;
    @FXML
    Text message;

    private Main main;
    private Stage connectStage;

    public void setMain(Main main){
        this.main = main;
    }
    public void setConnectStage(Stage connectStage){
        this.connectStage = connectStage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void getInfo() throws URISyntaxException {
        main.setSocket(address.getText().toString(),port.getText().toString());
        if (SocketHandler.getInstance().getSocket().getSession() == null){
            message.setVisible(true);
        }
        else {
            connectStage.close();
        }
    }

}
