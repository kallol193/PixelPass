package com.BekaarCompany.PixelPass.controller;

import com.BekaarCompany.PixelPass.entity.Booking;
import com.BekaarCompany.PixelPass.entity.Event;
import com.BekaarCompany.PixelPass.service.BookingService;
import com.BekaarCompany.PixelPass.service.EventService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/events/create")
    public ResponseEntity<?> createEvent(@RequestBody Event newevent){
        return new ResponseEntity<>(eventService.createEvent(newevent), HttpStatus.CREATED);
    }

    @GetMapping("/events/all")
    public ResponseEntity<?> getAllEvents(){
        ResponseEntity<?> eventList = eventService.getAllEvent();
        return new ResponseEntity<>(eventList,HttpStatus.FOUND);
    }

    @PatchMapping("/events/update/Id/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable ObjectId eventId,@RequestBody Event eventUpdate){
        eventService.updateEvent(eventId,eventUpdate);
        Event updatedFromDb = eventService.getEventById(eventId);
        return ResponseEntity.ok(String.format("Updated event :%s of Id :%s",updatedFromDb,eventId));
    }

    @DeleteMapping("/events/delete/{eventId}")
    public ResponseEntity<?> deleteEventByTitle(@PathVariable String eventTitle){
        eventService.deleteEvent(eventTitle);
        return ResponseEntity.ok(String.format("deleted event of title : %s",eventTitle));
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<?> allBookings(){
        List<Booking> allBookingsInDb = bookingService.getAllBookings();
        return ResponseEntity.ok(allBookingsInDb);
    }
}
