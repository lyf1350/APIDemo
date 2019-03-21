package com.test.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class WebSocketUtil {
    public static final Map<String, WebSocketSession> ONLINE_USER_SESSIONS=new ConcurrentHashMap();

    public static void sendMessage(String username, String message) {
        WebSocketSession session=ONLINE_USER_SESSIONS.get(username);
        log.info("send message user:"+username);
        if (session == null) {
            return;
        }

        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("sendMessage IOException ",e);
        }
    }

    public static void sendMessageAll(String message) {
        ONLINE_USER_SESSIONS.forEach((sessionId, session) -> sendMessage(sessionId, message));
    }
}
