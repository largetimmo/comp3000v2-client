package SmarterMonitor.socket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class Socket {
    private Session session;
    private MessageHandler messageHandler;

    public static interface MessageHandler{
        public void handle(String message);
    }


    public Socket(URI serveraddr){
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        try {
            webSocketContainer.connectToServer(this,serveraddr);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
    }

    @OnClose
    public void onClose(Session session){
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message){
        if(messageHandler!=null){
            messageHandler.handle(message);
        }
    }
    public void setMessageHandler(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }
    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

}
