package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ventas")
public class VentaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "sale_number", unique = true)
  private String saleNumber;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "vendedor_id")
  private UUID vendedorId;

  @Column(name = "customer_id")
  private UUID customerId;

  @Column(name = "almacen_id")
  private UUID almacenId;

  @OneToMany(mappedBy = "venta",
          cascade = CascadeType.ALL,
          fetch = FetchType.EAGER)
  private List<VentaItemEntity> items;

  @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "discount", precision = 10, scale = 2)
  private BigDecimal discount;

  @Column(name = "tax", precision = 10, scale = 2)
  private BigDecimal tax;

  @Column(name = "total", nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "status")
  private String status;

  @Column(name = "notes")
  private String notes;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
