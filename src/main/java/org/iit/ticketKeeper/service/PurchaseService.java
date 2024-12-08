package org.iit.ticketKeeper.service;

import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.PurchaseHistory;
import org.iit.ticketKeeper.entity.PurchaseEntity;
import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.entity.UserEntity;
import org.iit.ticketKeeper.repository.PurchaseRepository;
import org.iit.ticketKeeper.repository.SessionRepository;
import org.iit.ticketKeeper.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public void savePurchase(Long id, String email, Integer qty) {

        System.out.println("this is save purchse ");
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            System.out.println("thiss is user represent");
            Optional<PurchaseEntity> pastPurchase = purchaseRepository.findBySessionIdAndUserId(id, user.get().getId());

            PurchaseEntity purchase = new PurchaseEntity();
            purchase.setSessionId(id);
            purchase.setUserId(user.get().getId());

            System.out.println("thi is entity  case ....");
            if(pastPurchase.isPresent()) {
                System.out.println(" /////////////// /////////////  this is past puchase : /////////////////////////////////");
                Integer newQty = pastPurchase.get().getQty() + qty;
                purchase.setQty(newQty);
                purchase.setId(pastPurchase.get().getId());
            } else {
                System.out.println("else part is working ");
                purchase.setQty(qty);
            }

            System.out.println("befor save");
            System.out.println(purchase);
            purchaseRepository.save(purchase);
            System.out.println("after save");
        }

    }

    public List<PurchaseHistory> getPurchaseHistory(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        List<PurchaseHistory> purchases = new ArrayList<>();

        if (user.isPresent()) {
            List<PurchaseEntity> purchaseEntities = purchaseRepository.findByUserId(user.get().getId());
            System.out.println("this is entities : "+ purchaseEntities);
            if(!purchaseEntities.isEmpty()){
                for (PurchaseEntity purchase : purchaseEntities) {

                    Optional<SessionEntity> session = sessionRepository.findById(purchase.getSessionId());

                    if (session.isPresent()){
                        System.out.println("this is session " + session.get().getEventName());
                        PurchaseHistory purchaseHistory = new PurchaseHistory();

                        purchaseHistory.setId(purchase.getId());
                        purchaseHistory.setEventName(session.get().getEventName());
                        purchaseHistory.setQty(purchase.getQty());
                        purchaseHistory.setDate(session.get().getEventDate());
                        purchaseHistory.setCategory(session.get().getCategory());
                        purchaseHistory.setPrice(session.get().getTicketPrice());
                        purchaseHistory.setImageType(session.get().getImageType());
                        purchaseHistory.setImage(session.get().getTicketImage());

                        purchases.add(purchaseHistory);
                    }

                }
            }

        }
        System.out.println("this is all purchases : " + purchases);
        return purchases;
    }


}
