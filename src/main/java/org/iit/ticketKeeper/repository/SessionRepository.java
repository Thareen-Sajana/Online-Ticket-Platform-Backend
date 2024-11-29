package org.iit.ticketKeeper.repository;

import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.protection.SessionProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    List<SessionProjection> findBy();
    Optional<SessionEntity> findById(Long id);

}
