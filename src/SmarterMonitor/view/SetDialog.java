package SmarterMonitor.view;

import SmarterMonitor.Main;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;




public class SetDialog {
    private Stage setStage;
    private RootLayout rootLayout;
    private Main main;


    @FXML
    private TextField rateSecond;
    @FXML
    Text message;

    public SetDialog(){

    }

    public void setDialogStage(Stage setStage) {
        this.setStage = setStage;
    }
    public void setRootLayout(RootLayout rootLayout){
        this.rootLayout = rootLayout;
    }

    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    private void initialize() {
    }
    @FXML
    private void getRate(){
        if (Integer.parseInt(rateSecond.getText().trim().toString()) > 30){
            message.setVisible(true);
            return;
        }
        main.setRate(Integer.parseInt(rateSecond.getText().trim().toString()),main.getNewTimer());
        setStage.close();
    }

}
