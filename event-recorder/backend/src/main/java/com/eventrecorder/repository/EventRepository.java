package com.eventrecorder.repository;

import com.eventrecorder.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Event> findByTitleContainingOrContentContaining(String title, String content);
    
    @Query("SELECT e FROM Event e WHERE YEAR(e.eventDate) = :year AND MONTH(e.eventDate) = :month")
    List<Event> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    @Query("SELECT e FROM Event e WHERE YEAR(e.eventDate) = :year")
    List<Event> findByYear(@Param("year") int year);
    
    List<Event> findByCategory(String category);
}
