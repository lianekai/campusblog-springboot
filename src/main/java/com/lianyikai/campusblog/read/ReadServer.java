package com.lianyikai.campusblog.read;

import com.lianyikai.campusblog.service.ReadRecordService;
import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.ReadRecord;
import com.lianyikai.campusblog.pojo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
@ServerEndpoint("/detail/read/{aid}/{uid}")
@Component
public class ReadServer {
    private static ReadRecordService readRecordService;
    private static final int DIV_UNIT = 1000;
    static Log log= LogFactory.getLog(ReadServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<ReadServer> webSocketSet = new CopyOnWriteArraySet<ReadServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收文章id&用户id
    private String aid;
    private String uid;
    // 开始时间
    private long timestamp;

    @Autowired
    public void setReadRecordService(ReadRecordService readRecordService) {
        ReadServer.readRecordService = readRecordService;
    }

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("aid") String aid, @PathParam("uid") String uid) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        this.aid=aid;
        this.uid=uid;
        this.timestamp = new Date().getTime();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            ReadRecord record = new ReadRecord();
            long duration = (new Date().getTime() - this.timestamp)/DIV_UNIT;
            int user = Integer.valueOf(uid);
            if (user > 0 && duration > 60) {
                record.setUser(new User(user));
                record.setArticle(new Article(Integer.valueOf(aid)));
                record.setDuration(duration);
                record.setCreatedAt(new Date());
                readRecordService.read(record);
                webSocketSet.remove(this);  //从set中删除
                subOnlineCount();           //在线数减1
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        //群发消息
        for (ReadServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (ReadServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.aid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ReadServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ReadServer.onlineCount--;
    }
}
