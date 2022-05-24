package com.example.databases.model.DTO;

import com.example.databases.model.EventRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection;

@AllArgsConstructor
@Data
public class EventDTO {

    private String  eventName;

    private DayOfWeek dayOfWeek;

    private LocalTime eventTime;

    private String description;

    private EventRole eventRole;

}
