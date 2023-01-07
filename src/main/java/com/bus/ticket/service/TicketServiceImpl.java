package com.bus.ticket.service;

import com.bus.ticket.model.Bus;
import com.bus.ticket.model.Ticket;
import com.bus.ticket.model.Users;
import com.bus.ticket.repository.BusRepository;
import com.bus.ticket.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;

    private final BusRepository busRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, BusRepository busRepository) {
        this.busRepository = busRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        log.debug("Request to save Ticket : {}", ticket);
        if (ticket.getSeatAdult() == null){
            ticket.setSeatAdult(0);
        }
        if (ticket.getSeatChild() == null){
            ticket.setSeatChild(0);
        }
        if (ticket.getBus() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bus can not be empty!");
        }
        if (ticket.getUsers() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User can not be empty!");
        }
        Optional<Bus> bus = busRepository.findById(ticket.getBus().getId());
        if (bus.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bus can not be empty!");
        }
        LocalDateTime before10Mintue = bus.get().getDepartureDateTime().minusMinutes(10);
        if (LocalDateTime.now().isAfter(before10Mintue)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can buy the ticket before 10 minute only!");
        }
        Integer totalTicket = ticket.getSeatAdult() + ticket.getSeatChild();
        if (totalTicket < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please add some thicket!");
        }
        Optional<Bus> bus1 = busRepository.findById(ticket.getBus().getId());
        if (!bus1.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bus not found!");
        }
        if (bus1.get().getMaxSeat() < totalTicket){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not available seat here!");
        }
        bus1.get().setMaxSeat(bus1.get().getMaxSeat() - totalTicket);
        busRepository.save(bus1.get());
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
    public List<Ticket> findAllTicketByUser(Users users) {
        return ticketRepository.findAllByUsers(users);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        return ticketRepository.findById(id);
    }

    @Override
    public void delete(Long id, Long busId) {
        log.debug("Request to delete Ticket : {}", id);
        Optional<Bus> bus = busRepository.findById(busId);
        if (bus.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bus can not be empty!");
        }
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket can not be empty!");
        }
        bus.get().setMaxSeat(bus.get().getMaxSeat() + (ticket.get().getSeatAdult() + ticket.get().getSeatChild()));
        busRepository.save(bus.get());
        ticketRepository.deleteById(id);
    }
}
