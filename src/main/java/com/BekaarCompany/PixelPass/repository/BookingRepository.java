package com.BekaarCompany.PixelPass.repository;

import com.BekaarCompany.PixelPass.entity.Booking;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, ObjectId> {
    List<Booking> findByUserId(String userId);
}
