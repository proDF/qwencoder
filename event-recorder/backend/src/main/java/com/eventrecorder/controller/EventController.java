package com.eventrecorder.controller;

import com.eventrecorder.entity.Event;
import com.eventrecorder.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    
    @Autowired
    private EventService eventService;
    
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(id, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/month/{year}/{month}")
    public List<Event> getEventsByMonth(@PathVariable int year, @PathVariable int month) {
        return eventService.getEventsByMonth(year, month);
    }
    
    @GetMapping("/year/{year}")
    public List<Event> getEventsByYear(@PathVariable int year) {
        return eventService.getEventsByYear(year);
    }
    
    @GetMapping("/search")
    public List<Event> searchEvents(@RequestParam String keyword) {
        return eventService.searchEvents(keyword);
    }
    
    @GetMapping("/range")
    public List<Event> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return eventService.getEventsByDateRange(startDate, endDate);
    }
}
