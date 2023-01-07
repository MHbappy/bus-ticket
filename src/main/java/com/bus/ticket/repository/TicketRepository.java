package com.bus.ticket.repository;

import com.bus.ticket.model.Ticket;
import com.bus.ticket.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByUsers(Users users);
}
