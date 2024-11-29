package org.iit.ticketKeeper.protection;

public interface SessionProjection {
    String getEventName();
    String getEmail();
    Double getTicketPrice();
    Integer getTotalTicket();

    //byte[] getTicketImage();
    String getImageType();
}
