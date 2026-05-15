package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {
    private UUID id;

    // === EMPRESA ===
    private String businessName;
    private String ruc;
    private String address;
    private String phone;
    private String currency;
    private Double taxPercent;
    private String ticketFooterMessage;
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
    private String yapeQrUrl;
    private String plinQrUrl;
    private String yapePhone;
    private String plinPhone;

    // === MÓDULOS (FEATURE FLAGS) ===
    private Boolean moduleSizes;       // Tallas
    private Boolean moduleColors;      // Colores
    private Boolean moduleBrand;       // Marca
    private Boolean moduleExpiration;  // Fecha de vencimiento
    private Boolean moduleSerial;      // Número de serie
    private Boolean moduleWarranty;    // Garantía

    public void actualizar(Configuracion c) {
        if (c.getBusinessName() != null) this.businessName = c.getBusinessName();
        if (c.getRuc() != null) this.ruc = c.getRuc();
        if (c.getAddress() != null) this.address = c.getAddress();
        if (c.getPhone() != null) this.phone = c.getPhone();
        if (c.getCurrency() != null) this.currency = c.getCurrency();
        if (c.getTaxPercent() != null) this.taxPercent = c.getTaxPercent();
        if (c.getTicketFooterMessage() != null) this.ticketFooterMessage = c.getTicketFooterMessage();
        if (c.getLogoUrl() != null) this.logoUrl = c.getLogoUrl();
        if (c.getTicketPrinter() != null) this.ticketPrinter = c.getTicketPrinter();
        if (c.getAutoPrint() != null) this.autoPrint = c.getAutoPrint();
        if (c.getEmailNotifications() != null) this.emailNotifications = c.getEmailNotifications();
        if (c.getLowStockAlerts() != null) this.lowStockAlerts = c.getLowStockAlerts();
        if (c.getLowStockThreshold() != null) this.lowStockThreshold = c.getLowStockThreshold();
        if (c.getAllowNegativeStock() != null) this.allowNegativeStock = c.getAllowNegativeStock();
        if (c.getExchangeRateUSD() != null) this.exchangeRateUSD = c.getExchangeRateUSD();
        if (c.getExchangeRateEUR() != null) this.exchangeRateEUR = c.getExchangeRateEUR();
        if (c.getYapeQrUrl() != null) this.yapeQrUrl = c.getYapeQrUrl();
        if (c.getPlinQrUrl() != null) this.plinQrUrl = c.getPlinQrUrl();
        if (c.getYapePhone() != null) this.yapePhone = c.getYapePhone();
        if (c.getPlinPhone() != null) this.plinPhone = c.getPlinPhone();
        if (c.getModuleSizes() != null) this.moduleSizes = c.getModuleSizes();
        if (c.getModuleColors() != null) this.moduleColors = c.getModuleColors();
        if (c.getModuleBrand() != null) this.moduleBrand = c.getModuleBrand();
        if (c.getModuleExpiration() != null) this.moduleExpiration = c.getModuleExpiration();
        if (c.getModuleSerial() != null) this.moduleSerial = c.getModuleSerial();
        if (c.getModuleWarranty() != null) this.moduleWarranty = c.getModuleWarranty();
    }
}
