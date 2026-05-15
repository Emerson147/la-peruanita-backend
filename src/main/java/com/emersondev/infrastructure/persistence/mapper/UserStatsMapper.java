package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.UserStats;
import com.emersondev.infrastructure.persistence.entity.UserStatsEntity;
import org.springframework.stereotype.Component;

@Component
public class UserStatsMapper {

  public UserStatsEntity toEntity(UserStats s) {
    if (s == null) return null;
    UserStatsEntity e = new UserStatsEntity();
    e.setId(s.getId());
    e.setUserId(s.getUserId());
    e.setTotalPoints(s.getTotalPoints());
    e.setLevel(s.getLevel());
    e.setCurrentStreak(s.getCurrentStreak());
    e.setLongestStreak(s.getLongestStreak());
    e.setTotalSalesCompleted(s.getTotalSalesCompleted());
    e.setTotalRevenueGenerated(s.getTotalRevenueGenerated());
    e.setJoinedAt(s.getJoinedAt());
    return e;
  }

  public UserStats toDomain(UserStatsEntity e) {
    if (e == null) return null;
    UserStats s = new UserStats();
    s.setId(e.getId());
    s.setUserId(e.getUserId());
    s.setTotalPoints(e.getTotalPoints());
    s.setLevel(e.getLevel());
    s.setCurrentStreak(e.getCurrentStreak());
    s.setLongestStreak(e.getLongestStreak());
    s.setTotalSalesCompleted(e.getTotalSalesCompleted());
    s.setTotalRevenueGenerated(e.getTotalRevenueGenerated());
    s.setJoinedAt(e.getJoinedAt());
    return s;
  }
}