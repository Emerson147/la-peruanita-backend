package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GoalJpaRepository extends JpaRepository<GoalEntity, UUID> {

  List<GoalEntity> findByUserId(UUID userId);
  List<GoalEntity> findByUserIdAndStatus(UUID userId, String status);
}
