package com.bus.ticket.service;


import com.bus.ticket.model.Ticket;
import com.bus.ticket.model.Users;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Ticket}.
 */
public interface TicketService {
    Ticket save(Ticket ticket);
    Optional<Ticket> partialUpdate(Ticket ticket);
    List<Ticket> findAllTicketByUser(Users users);
    List<Ticket> findAll();
    Optional<Ticket> findOne(Long id);
    void delete(Long id, Long busId);
}
