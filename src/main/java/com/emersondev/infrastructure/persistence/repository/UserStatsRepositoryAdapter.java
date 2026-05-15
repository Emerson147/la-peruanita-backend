package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.UserStats;
import com.emersondev.domain.repository.UserStatsRepository;
import com.emersondev.infrastructure.persistence.mapper.UserStatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatsRepositoryAdapter
        implements UserStatsRepository {

  private final UserStatsJpaRepository jpaRepository;
  private final UserStatsMapper mapper;

  @Override
  public UserStats save(UserStats stats) {
    return mapper.toDomain(
            jpaRepository.save(mapper.toEntity(stats)));
  }

  @Override
  public Optional<UserStats> findByUserId(UUID userId) {
    return jpaRepository.findByUserId(userId)
            .map(mapper::toDomain);
  }

  @Override
  public List<UserStats> findAll() {
    return jpaRepository.findAll().stream()
            .map(mapper::toDomain).toList();
  }
}