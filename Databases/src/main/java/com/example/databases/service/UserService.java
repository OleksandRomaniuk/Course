package com.example.databases.service;

import com.example.databases.exception.AdminDeleteException;
import com.example.databases.exception.RegistrationException;
import com.example.databases.model.*;
import com.example.databases.model.DTO.GeneralResponseDTO;
import com.example.databases.repository.*;
import com.example.databases.security.jwt.JwtProvider;
import com.example.databases.security.jwt.cache.event.OnUserLogoutSuccessEvent;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private static final Logger logger = LogManager.getLogger();
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    JwtProvider jwtProvider;


    public UserService() {

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int userId) {
        return userRepository.findOneById(userId);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User getUserByRequest(HttpServletRequest request) {
        String token = jwtProvider.getTokenFromRequest(request);
        String email = jwtProvider.getLoginFromToken(token);
        return userRepository.findUserByEmail(email);
    }

    public String changePassword(HttpServletRequest request, String oldPassword, String newPassword) {
        String token = jwtProvider.getTokenFromRequest(request);
        String email = jwtProvider.getLoginFromToken(token);

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found ");
        }
        if(passwordService.equalsPassword(oldPassword, user.getPassword())){
            if(!passwordService.isValidPassword(newPassword)){
                throw new RegistrationException("Password is not valid");
            }
            user.setPassword(passwordService.encodePassword(newPassword));
            userRepository.save(user);
            return "Password changed";
        }else{
            throw new RegistrationException("The old password is incorrect");
        }
    }
    public String deleteUser(HttpServletRequest request){
        String token = jwtProvider.getTokenFromRequest(request);
        String email = jwtProvider.getLoginFromToken(token);
        User user = userRepository.findUserByEmail(email);
        user.setStatus(false);
        userRepository.save(user);
        OnUserLogoutSuccessEvent logoutEventPublisher = new OnUserLogoutSuccessEvent(user.getEmail(),token);
        applicationEventPublisher.publishEvent(logoutEventPublisher);
        return "Account has been deleted";
    }
    public ResponseEntity<String> updateUserByToken(GeneralResponseDTO dto, HttpServletRequest request) {
        String token = jwtProvider.getTokenFromRequest(request);
        String email = jwtProvider.getLoginFromToken(token);
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setAdressa(dto.getAdressa());
            user.setDate(dto.getDate());
            user.setPhone_number1(dto.getPhone_number1());
            user.setWork_place(dto.getWork_place());
            user.setFaatherName(dto.getFaatherName());
            return new ResponseEntity<String>(HttpStatus.OK);
        } else return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

    }



}
