package com.BekaarCompany.PixelPass.service;

import com.BekaarCompany.PixelPass.entity.Booking;
import com.BekaarCompany.PixelPass.entity.Event;
import com.BekaarCompany.PixelPass.entity.User;
import com.BekaarCompany.PixelPass.exceptions.NoContentExistException;
import com.BekaarCompany.PixelPass.repository.BookingRepository;
import com.BekaarCompany.PixelPass.repository.EventRepository;
import com.mongodb.internal.time.Timeout;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RedisTemplate<String ,Object> redisTemplate;

    private static final String CACHE_KEY2 = "STORAGE";

    //  reads all booking of a user(cacheble)
    public List<Booking> getBookingByUserId(String userId){
        List<Booking> allbookingcache = (List<Booking>) redisTemplate.opsForValue().get(CACHE_KEY2);
        if (allbookingcache.isEmpty()){
            throw new NoContentExistException("no bookings available..");
        }List<Booking> allbooking = bookingRepository.findByUserId(userId);
        redisTemplate.opsForValue().set(CACHE_KEY2,allbooking,30, TimeUnit.MINUTES);
        return allbooking;
    }

    //read all booking of a Id(cacheble)
    public Booking getBookingById(ObjectId Id){
        Optional<Booking> booking = bookingRepository.findById(Id);
        return booking.orElse(null);
    }

    @Transactional
    public Booking updateBooking(ObjectId Id,Booking BookingUpdate){
        Optional<Booking> BookingInDb = Optional.ofNullable(bookingRepository.findById(Id).orElse(null));
        if (BookingInDb.isPresent()){
            Booking booking = BookingInDb.get();
            booking.setNumberOfTickets(BookingUpdate.getNumberOfTickets());
            return bookingRepository.save(booking);
        }throw new IllegalArgumentException("Booking not Found!");
    }

    public void deleteBooking(ObjectId id){
        Optional<Booking> bookingOptional = Optional.ofNullable(bookingRepository.findById(id)
                .orElseThrow(() -> new NoContentExistException(String.format("Booking not found of Id : %s", id))));
        if (bookingOptional.isPresent())
        {
            bookingRepository.deleteById(id);
        }}


    @Transactional
    public void bookEvent(String Eventtitle,Booking booking) {
        Event eventPosted = eventRepository.findEventByTitle(Eventtitle);
        if (eventPosted == null) {
            throw new RuntimeException("Event not found!");
        } else {
            Integer availTickets = eventPosted.getAvailableTickets();
            Booking booked = bookingRepository.save(booking);
            if (booking.getNumberOfTickets() > availTickets) {
                throw new RuntimeException("Booking ticket Amount exceeds Available Ticket Numbers!");
            }
            eventPosted.setAvailableTickets(availTickets - booked.getNumberOfTickets());
            eventRepository.save(eventPosted);
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookingList = (List<Booking>) redisTemplate.opsForValue().get(CACHE_KEY2);
        List<Booking> allbookings = bookingRepository.findAll();
        if (allbookings.isEmpty()){
            throw new NoContentExistException("no bookings left!");
        }
        redisTemplate.opsForValue().set(CACHE_KEY2,allbookings,30,TimeUnit.MINUTES);
        return allbookings;
    }
}
