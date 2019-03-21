package com.test.demo.config;

import com.alibaba.fastjson.JSON;
import com.test.demo.model.Message;
import com.test.demo.model.User;
import com.test.demo.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import static com.test.demo.util.WebSocketUtil.ONLINE_USER_SESSIONS;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("message:"+payload);

        Message m = JSON.parseObject(payload, Message.class);
        switch (m.getType()) {
            case "all":
                WebSocketUtil.sendMessageAll(payload);
                break;
            case "person":
                WebSocketUtil.sendMessage(m.getDest(), payload);
                break;
            default:
                session.sendMessage(new TextMessage( m.getContent()));
                break;
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (session.getAttributes().get("user") != null)
            ONLINE_USER_SESSIONS.put(((User) session.getAttributes().get("user")).getUsername(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("websocket closed");
        if (session.getAttributes().get("user") != null)
            ONLINE_USER_SESSIONS.remove(((User) session.getAttributes().get("user")).getUsername());
    }
}
