package org.iit.ticketKeeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportResponse {

    private Long sessionId;
    private String eventName;
    private Double ticketPrice;
    private Integer totalTicket;
    private Integer ticketPoolCapacity;
    private Integer currentPoolCapacity;
    private Double predictedEarning;
    private Double currentEarning;
}
