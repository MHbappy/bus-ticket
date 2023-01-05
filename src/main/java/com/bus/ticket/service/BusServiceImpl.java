package com.bus.ticket.service;

import com.bus.ticket.model.Bus;
import com.bus.ticket.repository.BusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Bus}.
 */
@Service
@Transactional
public class BusServiceImpl implements BusService {

    private final Logger log = LoggerFactory.getLogger(BusServiceImpl.class);

    private final BusRepository busRepository;

    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    public Bus save(Bus bus) {
        log.debug("Request to save Bus : {}", bus);
        return busRepository.save(bus);
    }

    @Override
    public Optional<Bus> partialUpdate(Bus bus) {
        log.debug("Request to partially update Bus : {}", bus);

        return busRepository
            .findById(bus.getId())
            .map(
                existingBus -> {
                    if (bus.getName() != null) {
                        existingBus.setName(bus.getName());
                    }
                    if (bus.getBusNumber() != null) {
                        existingBus.setBusNumber(bus.getBusNumber());
                    }
                    if (bus.getFromStation() != null) {
                        existingBus.setFromStation(bus.getFromStation());
                    }
                    if (bus.getToStation() != null) {
                        existingBus.setToStation(bus.getToStation());
                    }
                    if (bus.getMaxSeat() != null) {
                        existingBus.setMaxSeat(bus.getMaxSeat());
                    }
                    if (bus.getDepartureDateTime() != null) {
                        existingBus.setDepartureDateTime(bus.getDepartureDateTime());
                    }
                    if (bus.getArrivalDateTime() != null) {
                        existingBus.setArrivalDateTime(bus.getArrivalDateTime());
                    }
                    return existingBus;
                }
            )
            .map(busRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bus> findAll() {
        log.debug("Request to get all Buses");
        return busRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bus> findOne(Long id) {
        log.debug("Request to get Bus : {}", id);
        return busRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bus : {}", id);
        busRepository.deleteById(id);
    }

    @Override
    public List<Bus> searchBusListByName(String name) {
        return busRepository.findByNameContainingIgnoreCase(name);
    }
}
