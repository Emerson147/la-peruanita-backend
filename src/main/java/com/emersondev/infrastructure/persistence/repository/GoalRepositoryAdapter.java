package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Goal;
import com.emersondev.domain.repository.GoalRepository;
import com.emersondev.infrastructure.persistence.mapper.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoalRepositoryAdapter implements GoalRepository {

  private final GoalJpaRepository jpaRepository;
  private final GoalMapper mapper;

  @Override
  public Goal save(Goal goal) {
    return mapper.toDomain(
            jpaRepository.save(mapper.toEntity(goal)));
  }

  @Override
  public Optional<Goal> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Goal> findByUserId(UUID userId) {
    return jpaRepository.findByUserId(userId).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<Goal> findByUserIdAndStatus(
          UUID userId, String status) {
    return jpaRepository
            .findByUserIdAndStatus(userId, status).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}