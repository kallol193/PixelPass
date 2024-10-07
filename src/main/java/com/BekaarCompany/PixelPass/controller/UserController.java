package com.BekaarCompany.PixelPass.controller;

import com.BekaarCompany.PixelPass.entity.Booking;
import com.BekaarCompany.PixelPass.entity.Event;
import com.BekaarCompany.PixelPass.entity.User;
import com.BekaarCompany.PixelPass.service.BookingService;
import com.BekaarCompany.PixelPass.service.EventService;
import com.BekaarCompany.PixelPass.service.UserService;
import com.BekaarCompany.PixelPass.service.kafka.KafkaProducerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping("/events")
    public ResponseEntity<?> getAllEvent(){
        kafkaProducerService.sendMessage("Fetching all events");
        return eventService.getAllEvent();
    }

    @GetMapping("/eventId/{id}")
    public Event getEventById(@PathVariable ObjectId id){
        kafkaProducerService.sendMessage(String.format("Fetching event with ID: %s", id));
        return eventService.getEventById(id);
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> bookEvent(@PathVariable String EventTitle ,@RequestBody Booking booking){
         bookingService.bookEvent(EventTitle,booking);
        kafkaProducerService.sendMessage(String.format("User booked event: %s", EventTitle));
        return ResponseEntity.ok("Booking sucess");
    }

    @DeleteMapping("/delete/bookingId/{id}")
    public ResponseEntity<?> deleteBookingById(ObjectId id){
        bookingService.deleteBooking(id);
        kafkaProducerService.sendMessage(String.format("User canceled booking with ID: %s", id));
        return new ResponseEntity<>(String.format("Cancelled Booking by id %s",id),HttpStatus.OK);
    }
}
