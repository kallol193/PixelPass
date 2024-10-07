package com.BekaarCompany.PixelPass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "EventCollection")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    private ObjectId id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer availableTickets;
}
