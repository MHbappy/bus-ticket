package com.bus.ticket.service;

import com.bus.ticket.model.Ticket;
import com.bus.ticket.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        log.debug("Request to save Ticket : {}", ticket);
        return ticketRepository.save(ticket);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> findAll() {
        log.debug("Request to get all Tickets");
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> partialUpdate(Ticket ticket) {
        log.debug("Request to partially update Ticket : {}", ticket);

        return ticketRepository
                .findById(ticket.getId())
                .map(
                        existingTicket -> {
                            if (ticket.getSeatAdult() != null) {
                                existingTicket.setSeatAdult(ticket.getSeatAdult());
                            }
                            if (ticket.getSeatChild() != null) {
                                existingTicket.setSeatChild(ticket.getSeatChild());
                            }

                            return existingTicket;
                        }
                )
                .map(ticketRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        return ticketRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
    }
}
