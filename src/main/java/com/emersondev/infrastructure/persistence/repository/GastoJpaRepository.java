package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GastoJpaRepository
        extends JpaRepository<GastoEntity, UUID> {

  List<GastoEntity> findByCategory(String category);

  @Query("SELECT g FROM GastoEntity g " +
          "WHERE g.createdAt BETWEEN :desde AND :hasta " +
          "ORDER BY g.createdAt DESC")
  List<GastoEntity> findByFecha(
          @Param("desde") LocalDateTime desde,
          @Param("hasta") LocalDateTime hasta);
}