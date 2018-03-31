package SmarterMonitor;

import SmarterMonitor.controller.SystemController;
import SmarterMonitor.socket.MessageHandler;
import SmarterMonitor.socket.Socket;
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
    public static Main getInstance(){
        return instance;
    }
    private MainWindow mainWindow = new MainWindow();
    private int rate=15000;
    Timer mTimer = new Timer();
    TimerTask update;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ArrayList<Object> token = new ArrayList<Object>();
    private int position = -1;
    private Socket socket;
    private ObservableList<Process> processData = FXCollections.observableArrayList();
    ArrayList<Process> checkedProcess = new ArrayList<Process>();

    @Override
    public void start(Stage primaryStage) throws Exception{

        //mainWindow.init();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Smarter Monitor");
        this.primaryStage.show();
        socket = new Socket(new URI("ws://192.168.0.20:8080/ws/client"));
        MessageHandler messageHandler = new MessageHandler();
        socket.setMessageHandler(messageHandler);
        initRootLayout();
        mainWindow = setMainWindow();
        instance = this;


    }

    //Init the root layout
    private void initRootLayout(){
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
            controller.setSocket(socket);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //Set MainWindow
    private MainWindow setMainWindow(){
        try{
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
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }




    public void setCurrentPos(int pos){
        position = pos;
        mTimer.cancel();
        mTimer.purge();
        update.cancel();
        mTimer = new Timer();
        update = new DoInBackgroud();
        mTimer.schedule(update,1,rate);
    }

    //Set the refresh rate
    public void setRate(int second,Timer mTimer){
        rate = second*1000;
        mTimer.cancel();
        mTimer.purge();
        update.cancel();
        mTimer = new Timer();
        update = new DoInBackgroud();
        mTimer.schedule(update,1,rate);
    }



    //Get Data from System every RATE second.
    private void getData(){
        update = new DoInBackgroud();
        mTimer.schedule(update, 1, rate);
    }


    public Timer getNewTimer(){
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
            System.out.println("Testing Get");
            System.out.println(position);
            if (position == -1){
                return;
            }
            String currToken = token.get(position).toString();//TODO
            JSONObject message = new JSONObject();
            message.put("ACTION","GETPROCES");
            message.put("TARGET",currToken);
            message.put("DATA","");
            checkedProcess.clear();
            for (int i = 0; i< processData.size(); i++){
                if (processData.get(i).getNeedKill() == 2){
                    checkedProcess.add(processData.get(i));
                }
            }

            try {
                socket.sendMessage(message.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            processData.removeAll(processData);
            //JSONStr = DATA.getallprocesses_test();  //In Linux, this function should be DATA.getallprocesses() TODO CHANGE and sent token
                //JSONStr = JSONStr.substring(10, JSONStr.length() - 1);
                //Testing Code
                //System.out.println("Test");
                //System.out.println(JSONStr);
//                JSONArray jsonArray = JSONArray.fromObject(JSONStr);
//                Object[] os = jsonArray.toArray();
//                int num = -1;  //Check how many process in the Computer
//                processData.removeAll(processData);
//                for (int i = 0; i < os.length; i++) {
//                    JSONObject jsonObject = JSONObject.fromObject(os[i]);
//                    String checkMemory = jsonObject.get("memory").toString().substring(jsonObject.get("memory").toString().length()-3,jsonObject.get("memory").toString().length()-1);
//                    if (checkMemory.equals("kB")) {
//                        processData.add(new Process(jsonObject.get("name").toString(), Integer.parseInt(jsonObject.get("pid").toString()), jsonObject.get("owner").toString() + "/" + jsonObject.get("ownergrp").toString(), jsonObject.get("memory").toString(), Float.parseFloat(jsonObject.get("cpu").toString())));
//                        num++;
//                            //Testing Code
////                        System.out.println("Testing--------------------");
////                        System.out.println("Name: " + jsonObject.get("name"));
////                        System.out.println("pid: " + jsonObject.get("pid"));
////                        System.out.println("ownerInfo: " + jsonObject.get("owner").toString() + "/" + jsonObject.get("ownergrp").toString());
////                        System.out.println("Memory: " + jsonObject.get("memory"));
////                        System.out.println("cpu: " + jsonObject.get("cpu"));
////                        System.out.println("ObList: " + processData.get(num).getpName());
//                    } else {
//                        continue;
//                    }
                //}
                //mainWindow.setSelection(selectedPid);  //TODO in handler
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    mainWindow.checkProcess();
//                }
//            });

            //Testing Code
                //System.out.println(processData);
                //System.out.println("test");
            }

    }

    public ObservableList<Process> getProcessData(){
        return processData;
    }

    public void deleteProcessData(int pid){
        for (int i=0;i<processData.size();i++){
            if (processData.get(i).getpID() == pid){
                processData.remove(processData.get(i));
            }
        }
    }

    public void addToken(String token){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainWindow.addNewTab(token);
            }
        });

        this.token.add(token);
    }



    public void setNeedKill(int pid){
        for (int i=0; i<processData.size();i++){
            if (processData.get(i).getpID() == pid){
                processData.get(i).setNeedKill(2);
            }
        }
    }

    public void setProcessData(ObservableList<Process> processData) {
        this.processData = processData;
    }

    public ArrayList<Process> getCheckedProcess() {
        return checkedProcess;
    }

    public static void main(String[] args) {

        launch(args);
    }
}
