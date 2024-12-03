package org.iit.ticketKeeper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SessionManageEntity {

    @Id
    private Long sessionId;

    private String getEventName;
    private byte[] getTicketImage;
    private String getImageType;
    private String getEventDate;
    private String getCategory;
    private Boolean isStarted;
}
