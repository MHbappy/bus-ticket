package com.bus.ticket.controller;

import com.bus.ticket.model.Bus;
import com.bus.ticket.repository.BusRepository;
import com.bus.ticket.service.BusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BusResource {

    private final Logger log = LoggerFactory.getLogger(BusResource.class);

    private final BusService busService;

    private final BusRepository busRepository;

    public BusResource(BusService busService, BusRepository busRepository) {
        this.busService = busService;
        this.busRepository = busRepository;
    }

    @PostMapping("/buses")
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) throws URISyntaxException {
        log.debug("REST request to save Bus : {}", bus);
        if (bus.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A new ticket cannot already have an ID" );
//            throw new BadRequestAlertException("A new bus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bus result = busService.save(bus);
        return ResponseEntity
            .created(new URI("/api/buses/" + result.getId()))
            .body(result);
    }

    @PutMapping("/buses/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bus bus)
        throws URISyntaxException {
        log.debug("REST request to update Bus : {}, {}", id, bus);
        if (bus.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
        }
        if (!Objects.equals(id, bus.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
        }

        if (!busRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Entity not found" );
        }

        Bus result = busService.save(bus);
        return ResponseEntity
            .ok()
            .body(result);
    }

//    @PatchMapping(value = "/buses/{id}", consumes = "application/merge-patch+json")
//    public ResponseEntity<Bus> partialUpdateBus(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bus bus)
//        throws URISyntaxException {
//        log.debug("REST request to partial update Bus partially : {}, {}", id, bus);
//        if (bus.getId() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
//        }
//        if (!Objects.equals(id, bus.getId())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
//        }
//
//        if (!busRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id" );
//        }
//
//        Optional<Bus> result = busService.partialUpdate(bus);
//        return ResponseEntity.ok(result.get());
//    }

    @GetMapping("/buses")
    public List<Bus> getAllBuses() {
        log.debug("REST request to get all Buses");
        return busService.findAll();
    }

    @GetMapping("/search-bus/{name}")
    public List<Bus> searchBusByName(@PathVariable String name) {
        log.debug("REST request to get all Buses");
        return busService.searchBusListByName(name);
    }

    @GetMapping("/search-bus-by-schedule")
    public List<Bus> searchBusBySchedule(@RequestParam String from, @RequestParam String to, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.debug("REST request to get all Buses" + date.toString());
        return busService.findByToAndFromAndDate(from, to, date);
    }

    @GetMapping("/buses/{id}")
    public ResponseEntity<Bus> getBus(@PathVariable Long id) {
        log.debug("REST request to get Bus : {}", id);
        Optional<Bus> bus = busService.findOne(id);
        return ResponseEntity.ok(bus.get());
    }

    @DeleteMapping("/buses/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        log.debug("REST request to delete Bus : {}", id);
        busService.delete(id);
        return ResponseEntity
            .ok()
            .build();
    }
}
