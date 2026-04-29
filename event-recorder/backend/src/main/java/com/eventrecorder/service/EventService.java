package com.eventrecorder.service;

import com.eventrecorder.entity.Event;
import com.eventrecorder.repository.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    
    @Autowired
    private EventMapper eventMapper;
    
    public List<Event> getAllEvents() {
        return eventMapper.selectList(null);
    }
    
    public Optional<Event> getEventById(Long id) {
        return Optional.ofNullable(eventMapper.selectById(id));
    }
    
    public Event createEvent(Event event) {
        eventMapper.insert(event);
        return event;
    }
    
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventMapper.selectById(id);
        if (event == null) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        
        event.setTitle(eventDetails.getTitle());
        event.setContent(eventDetails.getContent());
        event.setCategory(eventDetails.getCategory());
        event.setEventDate(eventDetails.getEventDate());
        
        eventMapper.updateById(event);
        
        return event;
    }
    
    public void deleteEvent(Long id) {
        eventMapper.deleteById(id);
    }
    
    public List<Event> getEventsByMonth(int year, int month) {
        return eventMapper.findByYearAndMonth(year, month);
    }
    
    public List<Event> getEventsByYear(int year) {
        return eventMapper.findByYear(year);
    }
    
    public List<Event> searchEvents(String keyword) {
        return eventMapper.findByTitleContainingOrContentContaining(keyword, keyword);
    }
    
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventMapper.findByEventDateBetween(startDate, endDate);
    }
}
