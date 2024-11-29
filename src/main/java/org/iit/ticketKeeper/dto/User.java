package org.iit.ticketKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String country;
    private String contactNumber;
    private String password;
    private Boolean isVipCustomer;
    private String role;
}
