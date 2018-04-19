package SmarterMonitor;

import SmarterMonitor.controller.SystemController;
import SmarterMonitor.socket.MessageHandler;
import SmarterMonitor.socket.Socket;
import SmarterMonitor.socket.SocketHandler;
import SmarterMonitor.view.RootLayout;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import SmarterMonitor.view.MainWindow;
import SmarterMonitor.view.Process;


import javax.websocket.ClientEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    public static Main instance = null;

    public static Main getInstance() {
        return instance;
    }

    private MainWindow mainWindow = new MainWindow();
    private int rate = 5000;
    Timer mTimer = new Timer();
    TimerTask update;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ArrayList<Object> token = new ArrayList<Object>();
    private int position = -1;
    MessageHandler messageHandler;
    private ObservableList<Process> processData = FXCollections.observableArrayList();
    ArrayList<Process> checkedProcess = new ArrayList<Process>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //mainWindow.init();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Smarter Monitor");
        this.primaryStage.show();
        initRootLayout();
        mainWindow = setMainWindow();
        instance = this;


    }

    public boolean setSocket(String address, String port) {
        address = "ws://" + address + ":" + port + "/ws/client";
        try {
            SocketHandler.getInstance().init(new URI(address));
        } catch (Exception e) {
            return false;
        }
        messageHandler = new MessageHandler();
        SocketHandler.getInstance().setMessageHandler(messageHandler);
        return true;
    }

    //Init the root layout
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            //Show the scene
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            RootLayout controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Set MainWindow
    private MainWindow setMainWindow() {
        try {
            //Load MainWindow
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainWindow.fxml"));
            AnchorPane mainWindow = (AnchorPane) loader.load();
            //Set into the center of rootLayout
            rootLayout.setCenter(mainWindow);
            //Data of table view
            getData();
            MainWindow controller = loader.getController();
            //controller.setMain(this);
            controller.setFilter(this);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setCurrentPos(int pos) {
        position = pos;
        mTimer.cancel();
        mTimer.purge();
        update.cancel();
        mTimer = new Timer();
        update = new DoInBackgroud();
        mTimer.schedule(update, 1, rate);
    }

    public int getPosition() {
        return position;
    }

    //Set the refresh rate
    public void setRate(int second, Timer mTimer) {
        rate = second * 1000;
        mTimer.cancel();
        mTimer.purge();
        update.cancel();
        mTimer = new Timer();
        update = new DoInBackgroud();
        mTimer.schedule(update, 1, rate);
    }


    //Get Data from System every RATE second.
    private void getData() {
        update = new DoInBackgroud();
        mTimer.schedule(update, 1, rate);
    }


    public Timer getNewTimer() {
        return mTimer;
    }

    //Back Ground. A timerTask to get the process info from system
    class DoInBackgroud extends TimerTask {
        private float updatef = 1.0f;

        public void setUpdatef(float f) {
            updatef = f;
        }

        @Override
        public void run() {
            //String JSONStr;
            /* Testiong */
            //System.out.println("Testing Get");
            //System.out.println(position);
            if (position == -1) {
                return;
            }
            String currToken = token.get(position).toString();//TODO
            JSONObject message = new JSONObject();
            message.put("ACTION", "GETPROCES");
            message.put("TARGET", currToken);
            message.put("DATA", rate);


            try {
                SocketHandler.getInstance().sendMessage(message.toString());
                System.out.println("Sent Command: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public ObservableList<Process> getProcessData() {
        return processData;
    }

    public void deleteProcessData(int pid) {
        for (int i = 0; i < processData.size(); i++) {
            if (processData.get(i).getpID() == pid) {
                processData.remove(processData.get(i));
            }
        }
    }

    public void addToken(String token) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainWindow.addNewTab(token);
            }
        });

        this.token.add(token);
    }


    public void setNeedKill(int pid) {
        for (int i = 0; i < processData.size(); i++) {
            if (processData.get(i).getpID() == pid) {
                processData.get(i).setNeedKill(2);
            }
        }
    }

    public void setProcessData(ObservableList<Process> processData) {
        this.processData.removeAll(this.processData);
        this.processData.addAll(processData);
    }

    public ArrayList<Process> getCheckedProcess() {
        return checkedProcess;
    }

    public String getCurrToken(int pos) {
        return token.get(pos).toString();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
