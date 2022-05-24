package com.example.databases.repository;


import com.example.databases.model.Event;
import com.example.databases.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findEventByEventId(Integer i);
    Event findEventByEventName(String name);

}
