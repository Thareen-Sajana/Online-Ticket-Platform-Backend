package org.iit.ticketKeeper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.Session;
import org.iit.ticketKeeper.dto.SessionManage;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.protection.SessionManageProjection;
import org.iit.ticketKeeper.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository repository;
    private final ObjectMapper objectMapper;

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
            session.get().setTotalTicket(newTotalTickets);

            repository.save(session.get());
        }
    }



}
