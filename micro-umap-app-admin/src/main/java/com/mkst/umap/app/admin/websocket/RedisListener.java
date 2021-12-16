package com.mkst.umap.app.admin.websocket;

import com.google.gson.JsonObject;
import com.mkst.mini.systemplus.ws.server.MicroWebsocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class RedisListener extends KeyExpirationEventMessageListener {

    @Autowired
    private MicroWebsocketServer microWebsocketServer;

    private static Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

    public RedisListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        sendExpire(message.toString());


    }

    public void sendExpire(String key) {

        Matcher m = p.matcher(key);

        if(m.find()){
            try {
                JsonObject json = new JsonObject();
                json.addProperty("roomId", key.split("--")[0]);
                json.addProperty("startTime", key.split("--")[1]);
                json.addProperty("result", "ok");
                json.addProperty("flag", true);
                microWebsocketServer.appointSending("987", json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
