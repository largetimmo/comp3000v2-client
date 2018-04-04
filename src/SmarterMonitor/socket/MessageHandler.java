package SmarterMonitor.socket;


import SmarterMonitor.Main;
import SmarterMonitor.view.LoginDialog;
import SmarterMonitor.view.MainWindow;
import SmarterMonitor.view.Process;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MessageHandler implements Socket.MessageHandler {
    @Override
    public void handle(String message) {
        System.out.println(message);
        JSONObject jsonMessage = JSONObject.parseObject(message);
        Main main = Main.getInstance();
        LoginDialog loginDialog = LoginDialog.getInstance();
        MainWindow mainWindow = MainWindow.getInstance();
        ArrayList<Process> checkedProcess = main.getCheckedProcess();
        if (jsonMessage.get("ACTION").toString().equals("LOGIN")) {
            if (jsonMessage.get("DATA").toString().equals("SUCCESS")) {
                main.addToken(jsonMessage.get("TARGET").toString());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loginDialog.close();
                    }
                });
            } else {
                loginDialog.showMessage();
                System.out.println("Failed");
            }
        }

        if (jsonMessage.get("ACTION").toString().equals("GETPROCES")) {
            System.out.println(jsonMessage);
            ObservableList<Process> processData = FXCollections.observableArrayList();
            JSONArray jsonArray = JSONArray.parseArray(jsonMessage.get("DATA").toString().substring(10, jsonMessage.get("DATA").toString().length() - 1));
            Object[] os = jsonArray.toArray();
            int num = -1;
            int selectedPid = 0;
            if (main.getProcessData().size() != 0) {
                selectedPid = mainWindow.getSelectionPID();
            }
            checkedProcess.clear();
            for (int i = 0; i< main.getProcessData().size(); i++){
                if (main.getProcessData().get(i).getNeedKill() == 2){
                    checkedProcess.add(main.getProcessData().get(i));
                }
            }
            for (int i = 0; i < os.length; i++) {
                JSONObject jsonObject = JSONObject.parseObject(os[i].toString());
                String checkMemory = jsonObject.get("memory").toString().substring(jsonObject.get("memory").toString().length() - 3, jsonObject.get("memory").toString().length() - 1);
                boolean isAdd = false;
                if (checkMemory.equals("kB") && isAdd == false) {
                    for (int j = 0; j < checkedProcess.size(); j++) {
                        if (Integer.parseInt(jsonObject.get("pid").toString()) == checkedProcess.get(j).getpID() && Float.parseFloat(jsonObject.get("cpu").toString()) >= 80) {
                            processData.add(new Process(jsonObject.get("name").toString(), Integer.parseInt(jsonObject.get("pid").toString()), jsonObject.get("owner").toString() + "/" + jsonObject.get("ownergrp").toString(), jsonObject.get("memory").toString(), Float.parseFloat(jsonObject.get("cpu").toString()), 2));
                            num++;
                            isAdd = true;
                            break;
                        }
                    }
                    if (isAdd == false) {
                        processData.add(new Process(jsonObject.get("name").toString(), Integer.parseInt(jsonObject.get("pid").toString()), jsonObject.get("owner").toString() + "/" + jsonObject.get("ownergrp").toString(), jsonObject.get("memory").toString(), Float.parseFloat(jsonObject.get("cpu").toString())));
                        num++;
                        isAdd = true;
                    }
                } else {
                    continue;
                }

            }
            main.setProcessData(processData);
            mainWindow.setSelection(selectedPid);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mainWindow.checkProcess();

                }
            });
        }

    }
}
