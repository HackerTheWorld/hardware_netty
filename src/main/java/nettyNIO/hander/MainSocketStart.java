package nettyNIO.hander;

import nettyNIO.hander.socket.SocketStart;
import nettyNIO.hander.websocket.WebSocketStart;

public class MainSocketStart {
    
    public static void main(String[] args){
        SocketStart.startSocket(9000);
        WebSocketStart.startWebSocket(9001);
        System.out.println("socket ::"+9000);
        System.out.println("webSocket ::"+9001);
    }

}
