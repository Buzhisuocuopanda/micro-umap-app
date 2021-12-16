package com.mkst.umap.app.admin.websocket;

import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.framework.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lxc
 * @describe
 * @Date: 2019/4/6
 * @Version 1.0
 */
//@Component
//@ServerEndpoint("/webSocket/arraign")
//@Slf4j
//@CrossOrigin(maxAge = 3600)
//public class ArraignWebSocket {
//
//    private Session session;
//
//    // 储存session
//    //  private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();
//
//    private static CopyOnWriteArraySet<ArraignWebSocket> webSocketSet = new CopyOnWriteArraySet<ArraignWebSocket>();
//
//    /**
//     * 打开连接
//     *
//     * @param session
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        this.session = session;
//        webSocketSet.add(this);
//        log.info("【websocket消息】有新的连接，总数:{}", webSocketSet.size());
//    }
//
//    /**
//     * 关闭连接
//     */
//    @OnClose
//    public void OnClose() {
//        webSocketSet.remove(this);
//        log.info("【websocket消息】连接断开，总数:{}", webSocketSet.size());
//    }
//
//    /**
//     * 收到消息
//     *
//     * @param message
//     */
//    @OnMessage
//    public void onMessage(String message) {
//        //自己发送的消息不再发送给自己
//        webSocketSet.remove(this);
//        sendMessage(message);
//        webSocketSet.add(this);
//
//        try {
//            dealMessage2Redis(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        log.info("【websocket消息】收到客户端发来的消息:{}", message);
//    }
//
//    /**
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误");
//        error.printStackTrace();
//    }
//
//
//    private void dealMessage2Redis(String message) {
//        RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");
//        JSONObject msgJson = JSONObject.parseObject(message);
//        String key = String.format(msgJson.getString("roomId") + msgJson.getString("date") + msgJson.getString("time"));
//        if ("lock".equals(msgJson.getString("msg"))) {
//            redisTemplate.opsForValue().set(key, 1, 300, TimeUnit.SECONDS);
//        } else {
//            if (redisTemplate.hasKey(key)) {
//                redisTemplate.delete(key);
//            }
//        }
//    }
//
//    /**
//     * 发送消息
//     */
//    public void sendMessage(String message) {
//        for (ArraignWebSocket webSocket : webSocketSet) {
//            log.info("【websocket消息】广播消息, message={}", message);
//            try {
//                webSocket.session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();    // 异常只打印不抛出（抛出会回滚）
//            }
//        }
//    }
//
//}
