package org.iit.ticketKeeper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Purchase {

    private Long id;

    private Integer userId;
    private Integer qty;
    private Long sessionId;
}
