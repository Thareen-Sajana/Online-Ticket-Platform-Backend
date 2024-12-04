package org.iit.ticketKeeper.controller.customer;

import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.PurchaseHistory;
import org.iit.ticketKeeper.dto.SessionManage;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/ticket-history")
    public ResponseEntity<Map<String, List<PurchaseHistory>>> showTicketPurchases(@RequestBody User user) {
        Map<String, List<PurchaseHistory>> response = new HashMap<>();
        System.out.println("this is purchase history endpoint : *************************");
        List<PurchaseHistory> purchases = purchaseService.getPurchaseHistory(user.getEmail());

        response.put("data", purchases);
        return ResponseEntity.ok(response);
    }
}
