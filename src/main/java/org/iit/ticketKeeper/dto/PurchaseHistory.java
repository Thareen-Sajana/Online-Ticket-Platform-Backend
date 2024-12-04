package org.iit.ticketKeeper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseHistory {

    private Long id;
    private String eventName;
    private Integer qty;
    private String date;
    private String category;
    private Double price;
    private byte[] image;

}
