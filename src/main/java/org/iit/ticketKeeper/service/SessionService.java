package org.iit.ticketKeeper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.ReportResponse;
import org.iit.ticketKeeper.dto.Session;
import org.iit.ticketKeeper.dto.SessionManage;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.entity.PurchaseEntity;
import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.entity.UserEntity;
import org.iit.ticketKeeper.protection.BuyDetailProtection;
import org.iit.ticketKeeper.protection.SessionManageProjection;
import org.iit.ticketKeeper.repository.PurchaseRepository;
import org.iit.ticketKeeper.repository.SessionRepository;
import org.iit.ticketKeeper.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository repository;
    private final ObjectMapper objectMapper;
    private final WebSocketService webSocketService;
    private final ReportService reportService;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    public void saveSession(Session session) {

        try {
            SessionEntity sessionEntity = objectMapper.convertValue(session, SessionEntity.class);
            sessionEntity.setIsStarted(false);
            System.out.println("this is entity details :" + sessionEntity.getEventDate());
            System.out.println("this is entity details :" + sessionEntity.getEmail());
            repository.save(sessionEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Session> getSessionDetails(){

        List<SessionEntity> sessions = repository.findAllByIsStartedTrue();
        List<Session> allSessions = new ArrayList<>();

        if(!sessions.isEmpty()) {
            for (SessionEntity session : sessions){
                allSessions.add(objectMapper.convertValue(session, Session.class));
            }
        }
        return allSessions;
    }

    public Session getSessionById(Long id){
        Optional<SessionEntity> sessionEntity = repository.findBySessionIdAndIsStartedTrue(id);
        if(sessionEntity.isEmpty()) return null;

        return objectMapper.convertValue(sessionEntity.get(), Session.class);
    }

    public List<SessionManage> getSessionDataByEmail(User user) {
        System.out.println("this is email : " + user);
        List<SessionManageProjection> sessionEntityList = repository.findByEmail(user.getEmail());

        System.out.println("this is data : " + sessionEntityList);
        List<SessionManage> sessions = new ArrayList<>();

        if (!sessionEntityList.isEmpty()){
            for (SessionManageProjection session : sessionEntityList) {
                sessions.add(objectMapper.convertValue(session, SessionManage.class));
            }
        }
        return sessions;
    }

    public void removeSession(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }

    public void startSession(Long id) {
        Optional<SessionEntity> session = repository.findById(id);
        if(session.isPresent()){
            session.get().setIsStarted(true);
            repository.save(session.get());
        }
    }

    public void stopSession(Long id) {
        Optional<SessionEntity> session = repository.findById(id);
        if(session.isPresent()){
            session.get().setIsStarted(false);
            repository.save(session.get());
        }
    }

    public void updateTotalTicket(Long id, Integer qty) {
        Optional<SessionEntity> session = repository.findById(id);
        if(session.isPresent()){
            Integer newTotalTickets = session.get().getTotalTicket() - qty;

            if (newTotalTickets <= 0) newTotalTickets = 0;

            session.get().setTotalTicket(newTotalTickets);

            repository.save(session.get());
        }
    }

    public void updateTotalTicket(String sessionId) {
        Integer qty = webSocketService.fetchQtyForSession(sessionId);

        System.out.println("\n\n\n\n\n\n\n\n*************** this is the QTY : " + qty);
        webSocketService.sendQty(sessionId, qty);
    }


    public String customerType(String email) {
        Double total = 0.0;

        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.isEmpty()) return "Not a user";

        if(user.isPresent()){

            List<PurchaseEntity> purchases = purchaseRepository.findByUserId(user.get().getId());
            if(!purchases.isEmpty()){

                for (PurchaseEntity purchase : purchases) {
                    Optional<BuyDetailProtection> session = repository.findBySessionId(purchase.getSessionId());

                    if(session.isPresent()){
                        total += (session.get().getTicketPrice() * purchase.getQty());
                    }

                }
                if(total >= 50000) return "VIP Customer";
            }

        }
        return "Regular Customer";
    }

    public void reportData(Long sessionId, ReportResponse reportResponse){
        ReportResponse response = reportService.getReportData(sessionId);
        response.setCurrentPoolCapacity(reportResponse.getCurrentPoolCapacity());
        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\nthis is when buy :" +);
        webSocketService.sendReportData(sessionId, response);
    }


}
