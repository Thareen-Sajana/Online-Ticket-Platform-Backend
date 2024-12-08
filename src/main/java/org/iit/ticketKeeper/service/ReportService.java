package org.iit.ticketKeeper.service;

import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.ReportResponse;
import org.iit.ticketKeeper.entity.PurchaseEntity;
import org.iit.ticketKeeper.protection.BuyDetailProtection;
import org.iit.ticketKeeper.repository.PurchaseRepository;
import org.iit.ticketKeeper.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SessionRepository sessionRepository;
    private final PurchaseRepository purchaseRepository;

    public ReportResponse getReportData(Long sessionId) {

        ReportResponse response = new ReportResponse();

        Integer totalTickets;
        Double currentEarning = 0.0;

        Optional<BuyDetailProtection> session = sessionRepository.findBySessionId(sessionId);
        if (session.isPresent()){
            response.setSessionId(sessionId);
            response.setTicketPrice(session.get().getTicketPrice());
            response.setTicketPoolCapacity(session.get().getTicketPoolCapacity());
            response.setEventName(session.get().getEventName());
            totalTickets = session.get().getTotalTicket();

            List<PurchaseEntity> purchases = purchaseRepository.findBySessionId(sessionId);
            if (!purchases.isEmpty()){

                for (PurchaseEntity purchase : purchases) {

                    totalTickets += purchase.getQty();
                    currentEarning += purchase.getQty() * session.get().getTicketPrice();
                }
                response.setTotalTicket(totalTickets);
                response.setCurrentEarning(currentEarning);
                response.setPredictedEarning(totalTickets * session.get().getTicketPrice());
            }
        }

        return response;
    }

}
