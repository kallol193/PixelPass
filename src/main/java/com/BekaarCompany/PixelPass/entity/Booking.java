package com.BekaarCompany.PixelPass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "BookingCollection")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    private ObjectId id;
    private ObjectId userId;
    private ObjectId eventId;
    private Integer NumberOfTickets;
    private LocalDateTime bookingDate;
}
