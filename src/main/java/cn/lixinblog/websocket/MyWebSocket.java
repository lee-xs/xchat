package cn.lixinblog.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
@Component
public class MyWebSocket {

    private static final Logger log = LoggerFactory.getLogger(MyWebSocket.class);

    /**
     * 静态变量，用来记录当前在线连接数，应该将其设计为线程安全的
     */
    private static int onlineCount = 0;

    /**
     * 用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<MyWebSocket> myWebSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void opOpen(Session session){
        this.session = session;

        myWebSocketSet.add(this);
        log.warn("有新连接加入！当前在线人数为" + myWebSocketSet.size());

        this.session.getAsyncRemote().sendText("当前在线人数：" + myWebSocketSet.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        myWebSocketSet.remove(this);
        log.warn("有一连接关闭！当前在线人数为" + myWebSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session){
        System.err.println(message);
        broadcast(message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.error("发生错误！");
        error.printStackTrace();
    }

    /**
     * 群发消息
     *
     * 异步发送消息 getAsyncRemote()
     * 同步发送消息 getBasicRemote()
     */
    public void broadcast(String message){
        for (MyWebSocket item : myWebSocketSet){
            item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 减少在线人数
     */
    private void subOnlineCount(){
        MyWebSocket.onlineCount--;
    }

    /**
     * 增加在线人数
     */
    private void addOnlineCount(){
        MyWebSocket.onlineCount++;
    }

}
