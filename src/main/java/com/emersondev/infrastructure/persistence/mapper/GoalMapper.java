package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Goal;
import com.emersondev.infrastructure.persistence.entity.GoalEntity;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper {

  public GoalEntity toEntity(Goal g) {
    if (g == null) return null;
    GoalEntity e = new GoalEntity();
    e.setId(g.getId());
    e.setUserId(g.getUserId());
    e.setTitle(g.getTitle());
    e.setDescription(g.getDescription());
    e.setType(g.getType());
    e.setMetric(g.getMetric());
    e.setTarget(g.getTarget());
    e.setCurrent(g.getCurrent());
    e.setStartDate(g.getStartDate());
    e.setEndDate(g.getEndDate());
    e.setStatus(g.getStatus());
    e.setReward(g.getReward());
    return e;
  }

  public Goal toDomain(GoalEntity e) {
    if (e == null) return null;
    Goal g = new Goal();
    g.setId(e.getId());
    g.setUserId(e.getUserId());
    g.setTitle(e.getTitle());
    g.setDescription(e.getDescription());
    g.setType(e.getType());
    g.setMetric(e.getMetric());
    g.setTarget(e.getTarget());
    g.setCurrent(e.getCurrent());
    g.setStartDate(e.getStartDate());
    g.setEndDate(e.getEndDate());
    g.setStatus(e.getStatus());
    g.setReward(e.getReward());
    return g;
  }
}