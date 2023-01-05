package com.bus.ticket.controller;

import com.bus.ticket.model.Ticket;
import com.bus.ticket.repository.TicketRepository;
import com.bus.ticket.service.TicketService;
import org.apache.tomcat.util.http.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TicketResource {




    private final Logger log = LoggerFactory.getLogger(TicketResource.class);
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    public TicketResource(TicketService ticketService, TicketRepository ticketRepository) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
    }
//
    @PostMapping("/tickets")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) throws URISyntaxException {
        log.debug("REST request to save Ticket : {}", ticket);
        if (ticket.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A new ticket cannot already have an ID" );
        }
        Ticket result = ticketService.save(ticket);
        return ResponseEntity
            .created(new URI("/api/tickets/" + result.getId()))
            .body(result);
    }
//
//    @PutMapping("/tickets/{id}")
//    public ResponseEntity<Ticket> updateTicket(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ticket ticket) {
//        log.debug("REST request to update Ticket : {}, {}", id, ticket);
//        if (ticket.getId() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
//        }
//        if (!Objects.equals(id, ticket.getId())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
//        }
//
//        if (!ticketRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Entity not found" );
//        }
//
//        Ticket result = ticketService.save(ticket);
//        return ResponseEntity
//            .ok()
//            .body(result);
//    }
//

//    @PostMapping("/cancel-ticket")
//    public Voi cancelTicket(@PathVariable Long ticketId) throws URISyntaxException {
//        log.debug("REST request to save Ticket : {}", ticketId);
////        Ticket ticket = ticketService.findOne(ticketId).get();
//        ticketService.delete(ticketId);
//        return ResponseEntity
//                .created(new URI("/api/tickets/" + ticketId))
//                .body(ticketId);
//    }

    @GetMapping("/ticketsByUser")
    public List<Ticket> getAllTicketByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return ticketService.findAll();
    }


    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        log.debug("REST request to get all Tickets");
        return ticketService.findAll();
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Optional<Ticket> ticket = ticketService.findOne(id);
        return ResponseEntity.ok(ticket.get());
    }

    @DeleteMapping("/cancel/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.debug("REST request to delete Ticket : {}", id);
        ticketService.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
