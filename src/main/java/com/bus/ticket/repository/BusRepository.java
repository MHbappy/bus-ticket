package com.bus.ticket.repository;

import com.bus.ticket.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByNameContainingIgnoreCase(String name);
}
