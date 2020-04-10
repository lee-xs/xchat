package cn.lixinblog.websocket;

import cn.lixinblog.entity.Message;
import cn.lixinblog.entity.User;
import cn.lixinblog.service.UserService;
import cn.lixinblog.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

@ServerEndpoint("/websocket/{ip}")
@Component
public class MyWebSocket {

    /** ######################### ↓↓↓↓↓ ######################### */

    private static UserService userService;

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setUserService(UserService userService){
        MyWebSocket.userService  = userService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        MyWebSocket.redisTemplate = redisTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(MyWebSocket.class);

    /**
     * 用来存放每个客户端对应的MyWebSocket对象
     */
    public static CopyOnWriteArraySet<MyWebSocket> myWebSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 当前连接的IP地址
     */
    private String ip;

    /**
     * 当前连接的用户信息
     */
    private User user;

    /** ######################### ↑↑↑↑↑ ######################### */


    /** ######################### ↓↓↓↓↓ ######################### */

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void opOpen(Session session, @PathParam("ip") String ip){
        this.session = session;
        this.ip = ip;
        this.user = userService.findUserByIp(ip);
        addOnlineCount(ip);
        myWebSocketSet.add(this);

//        Set set = redisTemplate.opsForZSet().rangeWithScores("message", 0, -1);
//
//        Iterator iterator = set.iterator();
//        String message = "";
//        while (iterator.hasNext()) {
//            ZSetOperations.TypedTuple val = (ZSetOperations.TypedTuple) iterator.next();
//            message += String.valueOf(val.getValue());
//        }
//        broadcast(message);
        Set set = redisTemplate.opsForZSet().rangeWithScores("message", 0, -1);

        Iterator iterator = set.iterator();
        List<Message> list = new ArrayList<>();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple val = (ZSetOperations.TypedTuple) iterator.next();
            Message msg = (Message) val.getValue();
            list.add(msg);
        }
        broadcast(list);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("ip") String ip){
        subOnlineCount(ip);
        myWebSocketSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message){//, Session session
        String str = user.getName() != null? user.getName() : user.getIp();
        //String result = "<p style='color: red; margin-bottom: 5px;'>【" + str + "】" + user.getEquipment() + user.getAttendedMode() + "</p><p style='padding-left: 5px;'>" + message + "</p></br>";
        //String result = "<p style='color: red; margin-bottom: 5px;'>【" + str + "】" + CommonUtils.parseDate() + "<span style='display: none;'>" + System.currentTimeMillis() + "</span>" + "</p><p style='padding-left: 5px;'>" + message + "</p></br>";

        Message msg = new Message();
        msg.setUser(user);
        msg.setContent(message);
        msg.setNow(CommonUtils.parseDate());
        msg.setTimestamp(System.currentTimeMillis());
        broadcast(msg);
        redisTemplate.opsForZSet().add("message", msg, System.currentTimeMillis());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.error("发生错误！");
        error.printStackTrace();
    }

    /** ######################### ↑↑↑↑↑ ######################### */


    /** ######################### ↓↓↓↓↓ ######################### */

    /**
     * 群发消息
     *
     * 异步发送消息 getAsyncRemote()
     * 同步发送消息 getBasicRemote()
     */
    public void broadcast(String message){
        for (MyWebSocket myWebSocket : myWebSocketSet){
            myWebSocket.session.getAsyncRemote().sendText(message);
        }
    }

    public void broadcast(Message msg){
        for (MyWebSocket myWebSocket : myWebSocketSet){
            myWebSocket.session.getAsyncRemote().sendText(JSON.toJSONString(msg));
        }
    }

    public void broadcast(List list){
        for (MyWebSocket myWebSocket : myWebSocketSet){
//            synchronized (myWebSocket){
//                array.forEach(item -> {
//                    try {
//                        myWebSocket.session.getBasicRemote().sendText(JSON.toJSONString(item));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            myWebSocket.session.getAsyncRemote().sendText(JSON.toJSONString(list));
        }
    }

    /** ######################### ↑↑↑↑↑ ######################### */


    /** ######################### ↓↓↓↓↓ ######################### */


    /**
     * 当前服务器在线连接数
     */
    private static long online = 0;

    /**
     * 增加在线用户
     * @param ip
     */
    public synchronized void addOnlineCount(String ip){
        AtomicBoolean bool = new AtomicBoolean(true);
        myWebSocketSet.forEach(item -> {
            if(item.user.getIp().equals(ip)){
                bool.set(false);
                return;
            }
        });

        if(bool.get()){
            MyWebSocket.online++;
        }
    }

    /**
     * 减少在线用户
     * @param ip
     */
    public synchronized void subOnlineCount(String ip){
        int index = (int) myWebSocketSet.stream().filter(item -> item.user.getIp().equals(ip)).count();
        if(index == 1){
            MyWebSocket.online--;
        }
    }

    public synchronized static long getOnlineCount(){
        return MyWebSocket.online;
    }

    /** ######################### ↑↑↑↑↑ ######################### */
}
