package org.iit.ticketKeeper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.Session;
import org.iit.ticketKeeper.entity.SessionEntity;
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
            System.out.println("this is entity details :" + sessionEntity.getEventDate());
            System.out.println("this is entity details :" + sessionEntity.getEmail());
            repository.save(sessionEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Session> getSessionDetails(){

        List<SessionEntity> sessions = repository.findAll();
        List<Session> allSessions = new ArrayList<>();

        if(!sessions.isEmpty()) {
            for (SessionEntity session : sessions){
                allSessions.add(objectMapper.convertValue(session, Session.class));
            }
        }
        return allSessions;
    }

    public Session getSessionById(Long id){
        Optional<SessionEntity> sessionEntity = repository.findById(id);
        if(sessionEntity.isEmpty()) return null;

        return objectMapper.convertValue(sessionEntity.get(), Session.class);
    }

}
