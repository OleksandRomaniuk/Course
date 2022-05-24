package com.example.databases.security.jwt.cache;



import com.example.databases.security.jwt.JwtProvider;
import com.example.databases.security.jwt.cache.event.OnUserLogoutSuccessEvent;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCache {

    private ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
    private JwtProvider jwtProvider;

    @Autowired
    public TokenCache(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(1000)
                .build();
    }

    public void markLogoutEventForToken(OnUserLogoutSuccessEvent event){
        String token = event.getToken();
        if(tokenEventMap.containsKey(token)){
            System.out.println("Token is already present in cache");
        }else{
            Date tokenExpireDate = jwtProvider.getExpireDateFromToken(token);
            long ttlForToken = getTTLForToken(tokenExpireDate);
            tokenEventMap.put(token,event,ttlForToken, TimeUnit.SECONDS);
        }
    }

    public OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(Date date) {
        long secondAtExpiry = date.toInstant().getEpochSecond();
        long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }
}
