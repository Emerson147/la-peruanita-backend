package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.VentaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VentaJpaRepository extends JpaRepository<VentaEntity, UUID> {

  Optional<VentaEntity> findBySaleNumber(String saleNumber);

  List<VentaEntity> findByUserId(UUID userId);

  List<VentaEntity> findByStatus(String status);

  @Query("SELECT v FROM VentaEntity v WHERE v.createdAt BETWEEN :desde AND :hasta")
  List<VentaEntity> findByFecha(
          @Param("desde") LocalDateTime desde,
          @Param("hasta") LocalDateTime hasta);


  Page<VentaEntity> findAll(Pageable pageable);
  Page<VentaEntity> findByStatus(String status, Pageable pageable);

}
