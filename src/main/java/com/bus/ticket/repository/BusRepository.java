package com.bus.ticket.repository;

import com.bus.ticket.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByNameContainingIgnoreCase(String name);
    @Query(value = "SELECT * FROM bus WHERE LOWER(from_station) = LOWER(?1) AND LOWER(to_station) = LOWER(?2) AND DATE_FORMAT(arrival_date_time, '%Y-%m-%d') = ?3", nativeQuery = true)
    List<Bus> findByToAndFromAndDate(String from, String to, String date);

}
