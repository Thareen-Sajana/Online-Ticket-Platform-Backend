package org.iit.ticketKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalTicketSocketResponse {

    String sessionId;
    Integer qty;
}
