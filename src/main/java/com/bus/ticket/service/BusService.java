package com.bus.ticket.service;

import com.bus.ticket.model.Bus;
import java.util.List;
import java.util.Optional;

public interface BusService {
    Bus save(Bus bus);
    Optional<Bus> partialUpdate(Bus bus);
    List<Bus> findAll();
    Optional<Bus> findOne(Long id);
    void delete(Long id);
    List<Bus> searchBusListByName(String name);
}
