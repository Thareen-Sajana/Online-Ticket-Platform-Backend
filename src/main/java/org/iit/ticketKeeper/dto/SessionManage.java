package org.iit.ticketKeeper.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionManage {

    private Long sessionId;
    private String eventName;
    private byte[] ticketImage;
    private String imageType;
    private String eventDate;
    private String category;
    private Boolean isStarted;
}
