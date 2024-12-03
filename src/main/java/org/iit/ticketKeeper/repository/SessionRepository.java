package org.iit.ticketKeeper.repository;

import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.protection.BuyDetailProtection;
import org.iit.ticketKeeper.protection.SessionManageProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    List<SessionManageProjection> findBy();
    Optional<SessionEntity> findBySessionIdAndIsStartedTrue(Long id);

    List<SessionManageProjection> findByEmail(String email);

    List<SessionEntity> findAllByIsStartedTrue();

    Optional<BuyDetailProtection> findBySessionId(Long id);

}
