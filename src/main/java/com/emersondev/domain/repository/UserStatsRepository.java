package com.emersondev.domain.repository;

import com.emersondev.domain.model.UserStats;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatsRepository {
  UserStats save(UserStats stats);
  Optional<UserStats> findByUserId(UUID userId);
  List<UserStats> findAll();
}