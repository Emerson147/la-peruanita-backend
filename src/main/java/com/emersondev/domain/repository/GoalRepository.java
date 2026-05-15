package com.emersondev.domain.repository;

import com.emersondev.domain.model.Goal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository {
  Goal save(Goal goal);
  Optional<Goal> findById(UUID id);
  List<Goal> findByUserId(UUID userId);
  List<Goal> findByUserIdAndStatus(UUID userId, String status);
  void deleteById(UUID id);
}