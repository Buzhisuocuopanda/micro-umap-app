package com.mkst.umap.app.admin.websocket;


import com.google.gson.JsonObject;
import com.mkst.mini.systemplus.framework.util.SpringUtils;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.mini.systemplus.ws.client.WebsocketClientEvent;
import com.mkst.mini.systemplus.ws.server.MicroWebsocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Lazy(false)
public class WebsocketMsgListener {


    @Autowired
    private MicroWebsocketServer microWebsocketServer;
    @Autowired
    private ISysConfigService configService;

//    public static MicroWebsocketServer getMicroWebsocketServer(){
//        if(microWebsocketServer == null){
//            microWebsocketServer  = SpringUtils.getBean(MicroWebsocketServer.class);
//        }
//        return microWebsocketServer;
//    }
    private RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

    @EventListener
    public void processMsg(WebsocketClientEvent event) throws IOException {
        //lock.lock();
        //try {
            String msgBody = event.getSource().toString();

            System.out.println("msg: " + msgBody);
            String key = "";

            if(msgBody.contains("singleRoom")){
                msgBody = msgBody.replaceAll("\"", "");
                key = msgBody.split("=")[1];
                Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, key);
                System.out.println("key: " + flag);
                if(flag)
                    redisTemplate.expire(key, spanTime(), TimeUnit.MINUTES);
                JsonObject json = new JsonObject();
                json.addProperty("roomId", key.split("--")[0]);
                json.addProperty("startTime", key.split("--")[1]);
                json.addProperty("result", flag);
                json.addProperty("flag", false);
                microWebsocketServer.appointSending("987", json.toString());
            }
            if(msgBody.contains("cancel")){
                msgBody = msgBody.replaceAll("\"", "");
                key = msgBody.split("=")[1];
                redisTemplate.delete(key);
                JsonObject json = new JsonObject();
                json.addProperty("roomId", key.split("--")[0]);
                json.addProperty("startTime", key.split("--")[1]);
                json.addProperty("result", "ok");
                json.addProperty("flag", true);
                microWebsocketServer.appointSending("987", json.toString());
            }
//        }  finally {  lock.unlock();
////        }
//


    }

  /*  public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put();
        MsgDataResult.Msg msg = MsgDataResult.Msg.fromJson(msgBody);
    }*/

    public int spanTime(){

        String time = configService.selectConfigByKey("room_lock_time");

        return Integer.parseInt(time);
    }

}
