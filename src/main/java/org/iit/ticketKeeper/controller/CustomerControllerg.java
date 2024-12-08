package org.iit.ticketKeeper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerControllerg {

    @GetMapping("/cus")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello form protected endpoint CUSTOMER");
    }

}
