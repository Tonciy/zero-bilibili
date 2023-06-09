package cn.zeroeden.service.websocket;

import cn.zeroeden.domain.Danmu;
import cn.zeroeden.domain.constant.UserMomentConstant;
import cn.zeroeden.service.DanmuService;
import cn.zeroeden.service.util.RocketMQUtil;
import cn.zeroeden.service.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.mysql.cj.util.StringUtils;
import lombok.extern.log4j.Log4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author: Zero
 * @time: 2023/7/6
 * @description:
 */

@Log4j
@Service
@ServerEndpoint(("/imserver/{token}"))
public class WebSocketService {

    public static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    private Session session;

    private String sessionId;

    private Long userId;

    private static  ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(org.springframework.context.ApplicationContext ac){
        WebSocketService.APPLICATION_CONTEXT = ac;
    }

    /**
     * 定时通知前端在线人数（暂时未区分视频）
     * @throws IOException
     */
    @Scheduled(fixedRate = 5000)
    private void notiiceLIneCount() throws IOException{
        for (Map.Entry<String, WebSocketService> stringWebSocketServiceEntry : WebSocketService.WEBSOCKET_MAP.entrySet()) {
            WebSocketService webSocketService = stringWebSocketServiceEntry.getValue();
            if(webSocketService.getSession().isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount", ONLINE_COUNT.get());
                jsonObject.put("msg","当前在线人数为:" + ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    @OnOpen
    public void openSession(Session session, @PathParam("token") String token){
        try {
            this.userId = TokenUtil.verifyToken(token);
        }catch (Exception e){

        }
        this.session = session;
        this.sessionId = session.getId();
        if(WEBSOCKET_MAP.containsKey(this.sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        }else{
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        try {
            this.sendMessage("0");
        }catch (Exception e){

        }
    }

    @OnClose
    public void closeConnection(){
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }

    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        DefaultMQProducer danmusProducer = (DefaultMQProducer) APPLICATION_CONTEXT.getBean("danmusProducer");
        if(!StringUtils.isNullOrEmpty(message)){
            try {
                // 群发消息
                for (Map.Entry<String, WebSocketService> stringWebSocketServiceEntry : WEBSOCKET_MAP.entrySet()) {
                    WebSocketService value = stringWebSocketServiceEntry.getValue();
                    JSONObject jsonpObject = new JSONObject();
                    jsonpObject.put("message", message);
                    jsonpObject.put("sessionId", sessionId);
                    Message msg = new Message(UserMomentConstant.TOPIC_DANMUS, jsonpObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    RocketMQUtil.asyncSendMsg(danmusProducer, msg);
                }
                if(this.userId != null){
                    // 保存弹幕到数据库
                    Danmu danmu = JSONObject.parseObject(message, Danmu.class);
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());
                    DanmuService danmuService = (DanmuService) APPLICATION_CONTEXT.getBean("danmuService");
//                    danmuService.addDanmu(danmu);
                    danmuService.asyncAddDanmu(danmu);
                    // 保存弹幕到Redis
                    danmuService.addDanmusToRedis(danmu);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    @OnError
    public void onError(Throwable error){

    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
