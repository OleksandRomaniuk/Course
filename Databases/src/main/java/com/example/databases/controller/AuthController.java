package com.example.databases.controller;

import com.example.databases.exception.JwtAuthenticationException;
import com.example.databases.model.DTO.LoginDTO;
import com.example.databases.security.jwt.CustomUserDetails;
import com.example.databases.security.jwt.cache.CurrentUser;
import com.example.databases.service.AuthenticationService;
import com.example.databases.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationService authenticationService;
    UserService userService;

    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginUserForm(@ModelAttribute("loginDTO")LoginDTO loginDTO, Model model){
        model.addAttribute("loginDTO", loginDTO);
        return "user-login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO")LoginDTO loginDTO,
                        HttpServletResponse response){
        Map<String, String> res = new HashMap<>();
        try {
            String token = authenticationService.login(loginDTO);

            int userId = userService.findUserByEmail(loginDTO.getEmail()).getId();
            String path = "/api/user/"+userId;

            Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, token);
            //cookie.setPath(path);
            cookie.setPath("/");

            response.addCookie(cookie);

            return "redirect:/api/user/profile";

        }catch (AuthenticationException ex) {

            return "user-login";
        }catch (Exception exception) {
            exception.printStackTrace();
            return "user-login";
            //return ResponseEntity.badRequest().body(res);
        }

    }
    @PostMapping("/check")
    public ResponseEntity<?> checkTokenExpire(@RequestBody Map<String,String> request){
        Map<String, String> res = new HashMap<>();
        try{
            String message = authenticationService.checkExpiration(request.get("token"));
            res.put("message", message);
            return ResponseEntity.ok(res);
        }catch (JwtAuthenticationException | ExpiredJwtException e){
            res.put("message",e.getMessage());
            return ResponseEntity.status(401).body(res);
        }
    }

    @GetMapping("/reset")
    public String resetPassword(Model model){
        model.addAttribute("loginDTO", new LoginDTO());
        return "user-reset";
    }
}
