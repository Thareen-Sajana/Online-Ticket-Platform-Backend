package org.iit.ticketKeeper.service;

import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.ReportResponse;
import org.iit.ticketKeeper.dto.TotalTicketSocketResponse;
import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.repository.SessionRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRepository sessionRepository;

    public void sendQty(String sessionId, Integer qty){
        System.out.println("Session Id : " + sessionId + " Qty : " + qty);
        messagingTemplate.convertAndSendToUser(sessionId, "notification", new TotalTicketSocketResponse(sessionId, qty));
    }

    public Integer fetchQtyForSession(String sessionId) {
        Optional<SessionEntity> session = sessionRepository.findBySessionIdAndIsStartedTrue(Long.valueOf(sessionId));
        if (session.isPresent()) {
            return session.get().getTotalTicket();
        }
        return 0;
    }

    public void sendReportData(Long sessionId, ReportResponse reportResponse){
        // Simulate report generation (you can replace this with actual logic)
        //ReportResponse reportData = new ReportResponse(sessionId, "Report generated successfully!", 100);
        // Send the report data to "/report/<sessionId>"
        messagingTemplate.convertAndSend("/report/" + sessionId, reportResponse);
    }
}
