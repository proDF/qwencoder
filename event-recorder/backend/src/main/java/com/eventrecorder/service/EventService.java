package com.eventrecorder.service;

import com.eventrecorder.entity.Event;
import com.eventrecorder.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
    
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        event.setTitle(eventDetails.getTitle());
        event.setContent(eventDetails.getContent());
        event.setCategory(eventDetails.getCategory());
        event.setEventDate(eventDetails.getEventDate());
        
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    
    public List<Event> getEventsByMonth(int year, int month) {
        return eventRepository.findByYearAndMonth(year, month);
    }
    
    public List<Event> getEventsByYear(int year) {
        return eventRepository.findByYear(year);
    }
    
    public List<Event> searchEvents(String keyword) {
        return eventRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
    
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByEventDateBetween(startDate, endDate);
    }
}
