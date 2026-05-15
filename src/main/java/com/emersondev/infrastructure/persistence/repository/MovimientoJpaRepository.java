package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.MovimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimientoJpaRepository
        extends JpaRepository<MovimientoEntity, UUID> {

  List<MovimientoEntity> findByProductId(UUID productId);

  List<MovimientoEntity> findByType(String type);
  Page<MovimientoEntity> findByType(String type, Pageable pageable);

  @Query("SELECT m FROM MovimientoEntity m " +
          "WHERE m.createdAt BETWEEN :desde AND :hasta " +
          "ORDER BY m.createdAt DESC")
  List<MovimientoEntity> findByFecha(
          @Param("desde") LocalDateTime desde,
          @Param("hasta") LocalDateTime hasta);

  long countByType(String type);
}