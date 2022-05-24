package com.example.databases.service;

import com.example.databases.model.DTO.EventDTO;
import com.example.databases.model.DTO.EventUpdateDTO;
import com.example.databases.model.DTO.IdDTO;
import com.example.databases.model.Event;
import com.example.databases.model.EventRole;
import com.example.databases.model.User;
import com.example.databases.repository.EventRepository;
import com.example.databases.repository.UserRepository;
import com.example.databases.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Transactional
@Service
public class EventService {


    @Autowired
    private EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;

    public String createEvent(HttpServletRequest request , EventDTO dto){
        String token = jwtProvider.getTokenFromRequest(request);
        String email = jwtProvider.getLoginFromToken(token);
        User user = userRepository.findUserByEmail(email);

        Event c = new Event();
        c.setUser(user);
        c.setDescription(dto.getDescription());
        c.setEventRole(dto.getEventRole());
        c.setEventName(dto.getEventName());
        c.setDayOfWeek(dto.getDayOfWeek());
        c.setEventTime(dto.getEventTime());
        eventRepository.save(c);
        return  "Подію створено";
    }


    public String changeStatus(HttpServletRequest request, EventUpdateDTO dto){
        Event e = eventRepository.findEventByEventId(dto.getId());
        if(dto.getStatus().equals("IN_PROGRES"))
        e.setEventRole(EventRole.IN_PROGRES);
        if(dto.getStatus().equals("COMPLETE"))
            e.setEventRole(EventRole.COMPLETE);
        if(dto.getStatus().equals("NOT_STARTED"))
            e.setEventRole(EventRole.NOT_STARTED);
        eventRepository.save(e);
        return  "Статус змінено";
    }
    public String deleteEvent(HttpServletRequest request, EventUpdateDTO dto){
        Event e = eventRepository.findEventByEventId(dto.getId());
        eventRepository.delete(e);
        return  "Подію видалено";
    }
    public List<Event> findAllEvents(){
        return eventRepository.findAll();
    }
    public String updateEvents(Integer id,EventDTO dto){
        Event c = eventRepository.findEventByEventId(id);
        c.setDescription(dto.getDescription());
        c.setEventRole(dto.getEventRole());
        c.setEventName(dto.getEventName());
        c.setDayOfWeek(dto.getDayOfWeek());
        c.setEventTime(dto.getEventTime());
        eventRepository.save(c);
        return "Подію обновлено!";
    }
    
    
}
