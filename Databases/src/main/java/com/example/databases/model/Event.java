package com.example.databases.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection;

//@Component
@Entity
@Table(name="event")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Event {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="event_id")
    private Integer eventId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="event_name")
    private String  eventName;

    @Column(name="day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name="event_time")
    private LocalTime eventTime;

    @Column(name="description")
    private String description;

    @ElementCollection
    private Collection<Integer> weeks;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_role")
    private EventRole eventRole;

}
