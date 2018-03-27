package SmarterMonitor.socket;

import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class MessageHandler implements Socket.MessageHandler{
    @Override
    public void handle(String message) {
        System.out.println("Receive:"+message);
    }
}
