package com.example.databases.service;


import com.example.databases.exception.JwtAuthenticationException;
import com.example.databases.model.DTO.LoginDTO;
import com.example.databases.model.User;
import com.example.databases.repository.UserRepository;
import com.example.databases.security.jwt.CustomUserDetails;
import com.example.databases.security.jwt.JwtProvider;
import com.example.databases.security.jwt.cache.event.OnUserLogoutSuccessEvent;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;


@Service
@AllArgsConstructor
public class AuthenticationService {


    private final ApplicationEventPublisher applicationEventPublisher;


    private final AuthenticationManager authenticationManager;


    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;




    public String login(LoginDTO request) throws AuthenticationException {

        User user = userRepository.findUserByEmail(request.getEmail());

        if(user==null){
            throw new AuthenticationException("Email is incorrect");
        }
        if(!user.getStatus()){
            throw new AuthenticationException("User been deleted");
        }
       if(new BCryptPasswordEncoder().matches(request.getPassword(),user.getPassword())){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtProvider.generateAuthToken(authentication);
        }
        throw new AuthenticationException("Password is incorrect");
    }
    public String logout(HttpServletRequest request, CustomUserDetails user) {
        String token = jwtProvider.getTokenFromRequest(request);
        OnUserLogoutSuccessEvent logoutEventPublisher = new OnUserLogoutSuccessEvent(user.getUsername(),token);
        applicationEventPublisher.publishEvent(logoutEventPublisher);
        return "You have successfully logout";
    }

    public String checkExpiration(String token) {
        Date expireDateFromToken = jwtProvider.getExpireDateFromToken(token);
       try{
           if(expireDateFromToken.after(Date.from(Instant.now()))){
            return "Token is valid";
        }}catch (ExpiredJwtException e){
           throw new JwtAuthenticationException("Token is invalid");
       }
        throw new JwtAuthenticationException("Token is invalid");
    }

}
