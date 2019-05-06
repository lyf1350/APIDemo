package com.test.demo.config;

import com.test.demo.model.User;
import com.test.demo.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("session created");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            if(se.getSession().getAttribute("user")!=null){
                WebSocketSession session=WebSocketUtil.ONLINE_USER_SESSIONS.get(((User)se.getSession().getAttribute("user")).getUsername());
                if(session!=null)
                    session.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
