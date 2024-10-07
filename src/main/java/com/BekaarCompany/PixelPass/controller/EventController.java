package com.BekaarCompany.PixelPass.controller;

import com.BekaarCompany.PixelPass.entity.Event;
import com.BekaarCompany.PixelPass.exceptions.NoContentExistException;
import com.BekaarCompany.PixelPass.service.EventService;
import com.BekaarCompany.PixelPass.service.kafka.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@Tag(name = "EventController",description = "APIs for managing events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Operation(summary = "Get all events", description = "Retrieve all events in the system")
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents(){
        kafkaProducerService.sendMessage("Fetching all events");
        return eventService.getAllEvent();
    }

    @Operation(summary = "Get event by ID", description = "Retrieve an event by its unique ID")
    @GetMapping("/EventId/{id}")
    public ResponseEntity<?> getEventByEventId(@PathVariable ObjectId EventId){
        kafkaProducerService.sendMessage(String.format("Fetching event with ID: %s", EventId));
        Event eventfound = eventService.getEventById(EventId);
        if (eventfound!=null){
            return ResponseEntity.ok(eventfound);
        }kafkaProducerService.sendMessage(String.format("Event with ID: %s not found", EventId));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Get event by location", description = "Retrieve events available at a specific location")
    @GetMapping("/location/{location}")
    public ResponseEntity<?> getEventByLocation(@PathVariable String location){
        kafkaProducerService.sendMessage(String.format("Fetching events by location: %s", location));
        Optional<?> eventInDb = eventService.findEventByLocation(location);
        return ResponseEntity.ok(eventInDb);
    }


}
