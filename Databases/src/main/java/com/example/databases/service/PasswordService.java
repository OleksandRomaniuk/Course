package com.example.databases.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {


     public boolean isValidPassword(String password){
         String reqExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
         return password.matches(reqExp);
     }

     public String encodePassword(String password){
         return new BCryptPasswordEncoder().encode(password);
     }

     public boolean equalsPassword(String password,String userPassword){
        return new BCryptPasswordEncoder().matches(password,userPassword);
    }
}
