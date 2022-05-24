package com.example.databases.security.jwt.cache.event;

import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.util.Date;

public class OnUserLogoutSuccessEvent extends ApplicationEvent {

    private final static long serialVersionUID = 1;
    private final String email;
    private final String token;
    private final Date time;


    public OnUserLogoutSuccessEvent(String email,String token) {
        super(email);
        this.email = email;
        this.token = token;
        this.time = Date.from(Instant.now());
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public Date getTime() {
        return time;
    }
}
