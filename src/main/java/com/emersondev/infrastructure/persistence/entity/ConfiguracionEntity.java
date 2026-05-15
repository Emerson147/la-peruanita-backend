package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "configuracion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionEntity {

    @Id
    private UUID id;

    // === EMPRESA ===
    private String businessName;
    private String ruc;
    private String address;
    private String phone;
    private String currency;
    private Double taxPercent;
    @Column(length = 500)
    private String ticketFooterMessage;
    @Column(length = 1000)
    private String logoUrl;

    // === PREFERENCIAS POS ===
    private String ticketPrinter;
    private Boolean autoPrint;
    private Boolean emailNotifications;
    private Boolean lowStockAlerts;
    private Integer lowStockThreshold;
    private Boolean allowNegativeStock;

    // === MULTI-MONEDA ===
    private Double exchangeRateUSD;
    private Double exchangeRateEUR;

    // === QR PAGOS ===
    @Column(length = 1000)
    private String yapeQrUrl;
    @Column(length = 1000)
    private String plinQrUrl;
    private String yapePhone;
    private String plinPhone;

    // === MÓDULOS (FEATURE FLAGS) ===
    private Boolean moduleSizes;
    private Boolean moduleColors;
    private Boolean moduleBrand;
    private Boolean moduleExpiration;
    private Boolean moduleSerial;
    private Boolean moduleWarranty;
}
