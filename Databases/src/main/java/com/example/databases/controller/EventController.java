package com.example.databases.controller;

import com.example.databases.exception.JwtAuthenticationException;
import com.example.databases.model.DTO.EventDTO;
import com.example.databases.model.DTO.EventUpdateDTO;
import com.example.databases.model.DTO.LoginDTO;
import com.example.databases.model.Event;
import com.example.databases.security.jwt.CustomUserDetails;
import com.example.databases.security.jwt.JwtProvider;
import com.example.databases.security.jwt.cache.CurrentUser;
import com.example.databases.service.AuthenticationService;
import com.example.databases.service.EventService;
import com.example.databases.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    EventService eventService;

    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(HttpServletRequest request, @RequestBody EventDTO dto) {
        try {
            eventService.createEvent(request, dto);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.LOCKED);
        }
    }
    @PostMapping("/changeStatus")
    public ResponseEntity<String> changeStatus(HttpServletRequest request, @RequestBody EventUpdateDTO dto) {
        try {
            eventService.changeStatus(request, dto);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.LOCKED);
        }
    }
    @PostMapping("/updateEvents")
    public ResponseEntity<String> updateEvents(@PathVariable Integer id, @RequestBody EventDTO dto) {
        try {
            eventService.updateEvents(id, dto);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.LOCKED);
        }
    }

    @GetMapping("/AllEvents")
    public List<Event> getAllLEvents(){
        return eventService.findAllEvents();
    }
    @DeleteMapping("/deleteEvents")
    public ResponseEntity<String> deleteEvents(HttpServletRequest request, @RequestBody EventUpdateDTO dto){
        try {
            eventService.deleteEvent(request, dto);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.LOCKED);
        }
    }






}
