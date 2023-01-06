package com.bus.ticket.service;

import com.bus.ticket.model.Bus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BusService {
    Bus save(Bus bus);
    Optional<Bus> partialUpdate(Bus bus);
    List<Bus> findAll();
    List<Bus> findByToAndFromAndDate(String from, String to, LocalDate date);
    Optional<Bus> findOne(Long id);
    void delete(Long id);
    List<Bus> searchBusListByName(String name);
}
