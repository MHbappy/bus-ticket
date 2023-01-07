package com.bus.ticket.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A Bus.
 */
@Entity
@Table(name = "bus")
@Data
public class Bus {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "bus_number")
    private String busNumber;

    @Column(name = "from_station")
    private String fromStation;

    @Column(name = "to_station")
    private String toStation;

    @Column(name = "max_seat")
    private Integer maxSeat = 0;

    @Column(name = "departure_date_time")
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_date_time")
    private LocalDateTime arrivalDateTime;
}
