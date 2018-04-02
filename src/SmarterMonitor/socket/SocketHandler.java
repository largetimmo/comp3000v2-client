package SmarterMonitor.socket;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;



public class SocketHandler {


        private Socket socket = null;
        private static final SocketHandler instance = new SocketHandler();
        public static SocketHandler getInstance(){
            return instance;
        }

        private final String LOGIN_ACTION = "LOGIN";
        private final String GET_PROCESS_ACTION = "GETPROCES";
        private final String KILL_PROCESS_ACTION = "KILL";
        private final String Action_Tag = "ACTION";
        private final String Target_Tag = "TARGET";
        private final String Data_Tag = "DATA";
        private final String User_ID_Tag = "UID";
        private final String Password_Tag = "PWD";



        public void init(URI uri){
            socket = new Socket(uri);
        }

        public void sendMessage(String message) throws IOException {
            socket.sendMessage(message);
        }

        public void setMessageHandler(MessageHandler messageHandler){
                socket.setMessageHandler(messageHandler);
        }
}
