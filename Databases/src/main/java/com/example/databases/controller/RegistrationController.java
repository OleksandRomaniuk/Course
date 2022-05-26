package com.example.databases.controller;


import com.example.databases.exception.RegistrationException;
import com.example.databases.model.DTO.UserDTO;
import com.example.databases.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {


    @Autowired
    RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    @GetMapping
    public String createUserForm(@ModelAttribute("userDTO")  UserDTO userDTO){
        //model.addAttribute("userDTO", new());
        return "user-create";
    }

    @PostMapping
    String registration(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/registration");
            ResponseEntity<String> resEnt = new ResponseEntity<String>(headers, HttpStatus.FOUND);
            //return resEnt;
            return "user-create";
        }

        Map<String, String> res = new HashMap<>();

        try{
            String result = registrationService.registration(userDTO);
            res.put("message", result);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/auth/login");
            ResponseEntity<String> resEnt = new ResponseEntity<String>(headers,HttpStatus.FOUND);
            //return resEnt;
            return "redirect:/api/auth/login";
        }
        catch(RegistrationException e){
            res.put("message",e.getMessage());
            return e.getMessage();
            //return ResponseEntity.badRequest().body(res);
        }

    }
    //method for registration

}