package com.example.databases.service;


import com.example.databases.exception.RegistrationException;
import com.example.databases.model.DTO.UserDTO;
import com.example.databases.model.User;
import com.example.databases.model.UserRole;
import com.example.databases.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service @RequiredArgsConstructor
public class RegistrationService {


    private final  EmailService emailService;
    private final  PasswordService passwordService;

    private final UserRepository userRepository;




    public String registration(UserDTO userDTO) throws RegistrationException {

        String email = userDTO.getEmail();
        if(emailService.emailExist(email)){
            throw new RegistrationException("User with email = "+ email + " already exist");
        }
      User user = new User();
        user.setEmail(email);
        //checking user password is valid
        String password = userDTO.getPassword();
        if(!passwordService.isValidPassword(password)){
            throw new RegistrationException("Password is not valid");
        }
        user.setPassword(passwordService.encodePassword(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAdressa(userDTO.getAdressa());
        user.setDate(userDTO.getDate());
        user.setPhone_number1(userDTO.getPhone_number1());
        user.setWork_place(userDTO.getWork_place());
        user.setFaatherName(userDTO.getFaatherName());
        user.setStatus(true);
        user.setUserRole(UserRole.USER);
        userRepository.save(user);

        return "User created";
    }

}
