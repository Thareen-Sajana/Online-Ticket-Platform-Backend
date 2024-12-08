package org.iit.ticketKeeper.controller.customer;

import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.PurchaseHistory;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.service.PurchaseService;
import org.iit.ticketKeeper.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final PurchaseService purchaseService;
    private final SessionService sessionService;

    @PostMapping("/ticket-history")
    public ResponseEntity<Map<String, List<PurchaseHistory>>> showTicketPurchases(@RequestBody User user) {
        Map<String, List<PurchaseHistory>> response = new HashMap<>();
        System.out.println("this is purchase history endpoint : *************************");
        List<PurchaseHistory> purchases = purchaseService.getPurchaseHistory(user.getEmail());

        response.put("data", purchases);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customer-type")
    public ResponseEntity<Map<String , String >> buy(@RequestBody User user) {
        Map<String, String > response = new HashMap<>();
        String customerType = sessionService.customerType(user.getEmail());

        response.put("data", customerType);
        return ResponseEntity.ok(response);
    }
}
