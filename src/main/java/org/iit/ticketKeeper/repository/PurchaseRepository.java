package org.iit.ticketKeeper.repository;

import org.iit.ticketKeeper.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    Optional<PurchaseEntity> findBySessionIdAndUserId(Long sessionId, Integer userId);
    List<PurchaseEntity> findByUserId(Integer userId);
}
