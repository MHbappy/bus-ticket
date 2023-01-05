package com.bus.ticket.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Data
public class Ticket{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_adult")
    private Integer seatAdult;

    @Column(name = "seat_child")
    private Integer seatChild;

    @ManyToOne
    private Users users;
}
