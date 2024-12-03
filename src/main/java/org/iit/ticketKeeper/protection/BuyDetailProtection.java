package org.iit.ticketKeeper.protection;

public interface BuyDetailProtection {
    Integer getTotalTicket();
    Integer getTicketPoolCapacity();
    Integer getTicketReleaseRate();
    Integer getCustomerReleaseRate();
}
