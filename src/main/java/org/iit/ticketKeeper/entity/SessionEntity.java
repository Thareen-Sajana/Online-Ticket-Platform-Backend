package org.iit.ticketKeeper.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "session")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    private String eventName;
    private String email;
    private Double ticketPrice;
    private Integer totalTicket;
    private Integer ticketPoolCapacity;
    private Integer ticketReleaseRate;
    private Integer customerReleaseRate;
    private Double vipDiscount;
    private String category;
    private String ticketType;
    private String eventDate;
    private String eventDescription;
    private String imageType;

    @Lob
    private byte[] ticketImage;
}
