package com.BekaarCompany.PixelPass.repository;

import com.BekaarCompany.PixelPass.entity.Event;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, ObjectId> {

    @Query(value = "{'location' : ?0}")
    Optional<Event> findEventByLocation(String location);

    @Query(value = "{'eventTitle' :?0}",delete = true)
    void deleteEventByTitle(String eventTitle);

    Event findEventByTitle(String eventtitle);
}
