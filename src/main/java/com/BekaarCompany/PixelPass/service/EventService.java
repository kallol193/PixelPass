package com.BekaarCompany.PixelPass.service;

import com.BekaarCompany.PixelPass.entity.Event;
import com.BekaarCompany.PixelPass.exceptions.NoContentExistException;
import com.BekaarCompany.PixelPass.repository.EventRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String ALL_EVENTS_CACHE_KEY ="ALL_EVENTS";
    private static final String EVENT_CACHE_KEY_PREFIX = "EVENT_";
    private static final String LOCATION_CACHE_KEY_PREFIX = "LOCATION_";

    @Transactional
    public Event createEvent(Event event){
        event.setDate(LocalDateTime.now());
        Event event1 = eventRepository.save(event);
        redisTemplate.delete(ALL_EVENTS_CACHE_KEY);
        return event1;
    }

    //read single event
    public Event getEventById(ObjectId id){
        String cacheKey = EVENT_CACHE_KEY_PREFIX + id.toString();
        Event event = (Event) redisTemplate.opsForValue().get(cacheKey);
        if (event == null) {
            event = eventRepository.findById(id).orElse(null);
            if (event != null) {
                // Caches the event data for 30 mins
                redisTemplate.opsForValue().set(cacheKey, event, 30, TimeUnit.MINUTES);
            }
        }return event;
    }

    //read all events
    public ResponseEntity<?> getAllEvent() {
        List<Event> allEvent = (List<Event>) redisTemplate.opsForValue().get(ALL_EVENTS_CACHE_KEY);
        if (allEvent == null || allEvent.isEmpty()) {
            // If not found in cache, queries the Db
            allEvent = eventRepository.findAll();
            if (allEvent.isEmpty()) {
                throw new NoContentExistException("No event is available");
            }
            // Caches all events for 30 mins
            redisTemplate.opsForValue().set(ALL_EVENTS_CACHE_KEY, allEvent, 30, TimeUnit.MINUTES);
        }
        return ResponseEntity.ok(allEvent);
    }


    @Transactional
    public Event updateEvent(ObjectId id,Event updateEvent){
        Optional<Event> existingEventOptional = eventRepository.findById(id);
        if (existingEventOptional.isPresent())
        {
            Event event = existingEventOptional.get();
            event.setDate(updateEvent.getDate());
            event.setDescription(updateEvent.getDescription());
            event.setTitle(updateEvent.getTitle());
            event.setAvailableTickets(updateEvent.getAvailableTickets());

            Event updatedEvent = eventRepository.save(event);
            // Clears the cache for this event and the all events cache
            String cacheKey = EVENT_CACHE_KEY_PREFIX + id.toString();
            redisTemplate.delete(cacheKey);
            redisTemplate.delete(ALL_EVENTS_CACHE_KEY);
            return updatedEvent;

        }
        return null;
    }

    public Optional<?> findEventByLocation(String location){
        String cacheKey = LOCATION_CACHE_KEY_PREFIX + location;
        Optional<?> cachedEvent = (Optional<?>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedEvent == null) {
            Optional<Event> eventOptional = eventRepository.findEventByLocation(location);
            if (eventOptional.isEmpty())throw new NoContentExistException("No location exists");
            else {
                redisTemplate.opsForValue().set(cacheKey,eventOptional,30,TimeUnit.MINUTES);
                return eventOptional;
            }
        }return cachedEvent;
    }

    @Transactional
    public void deleteEvent(String eventTitle){
        Event event = eventRepository.findEventByTitle(eventTitle);
        if (event == null) throw new RuntimeException("Event not found!");
        eventRepository.deleteEventByTitle(eventTitle);
        // Clears all relevant caches after deletion
        redisTemplate.delete(ALL_EVENTS_CACHE_KEY);
        redisTemplate.delete(EVENT_CACHE_KEY_PREFIX + event.getId());
    }
}
