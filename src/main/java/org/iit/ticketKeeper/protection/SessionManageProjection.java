package org.iit.ticketKeeper.protection;

public interface SessionManageProjection {
    Long getSessionId();
    String getEventName();
    byte[] getTicketImage();
    String getImageType();
    String getEventDate();
    String getCategory();
    Boolean getIsStarted();
}
