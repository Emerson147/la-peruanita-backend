package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Configuracion;
import com.emersondev.infrastructure.persistence.entity.ConfiguracionEntity;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionMapper {

    public Configuracion toDomain(ConfiguracionEntity e) {
        if (e == null) return null;
        return Configuracion.builder()
                .id(e.getId())
                .businessName(e.getBusinessName())
                .ruc(e.getRuc())
                .address(e.getAddress())
                .phone(e.getPhone())
                .currency(e.getCurrency())
                .taxPercent(e.getTaxPercent())
                .ticketFooterMessage(e.getTicketFooterMessage())
                .logoUrl(e.getLogoUrl())
                .ticketPrinter(e.getTicketPrinter())
                .autoPrint(e.getAutoPrint())
                .emailNotifications(e.getEmailNotifications())
                .lowStockAlerts(e.getLowStockAlerts())
                .lowStockThreshold(e.getLowStockThreshold())
                .allowNegativeStock(e.getAllowNegativeStock())
                .exchangeRateUSD(e.getExchangeRateUSD())
                .exchangeRateEUR(e.getExchangeRateEUR())
                .yapeQrUrl(e.getYapeQrUrl())
                .plinQrUrl(e.getPlinQrUrl())
                .yapePhone(e.getYapePhone())
                .plinPhone(e.getPlinPhone())
                .moduleSizes(e.getModuleSizes())
                .moduleColors(e.getModuleColors())
                .moduleBrand(e.getModuleBrand())
                .moduleExpiration(e.getModuleExpiration())
                .moduleSerial(e.getModuleSerial())
                .moduleWarranty(e.getModuleWarranty())
                .build();
    }

    public ConfiguracionEntity toEntity(Configuracion d) {
        if (d == null) return null;
        return ConfiguracionEntity.builder()
                .id(d.getId())
                .businessName(d.getBusinessName())
                .ruc(d.getRuc())
                .address(d.getAddress())
                .phone(d.getPhone())
                .currency(d.getCurrency())
                .taxPercent(d.getTaxPercent())
                .ticketFooterMessage(d.getTicketFooterMessage())
                .logoUrl(d.getLogoUrl())
                .ticketPrinter(d.getTicketPrinter())
                .autoPrint(d.getAutoPrint())
                .emailNotifications(d.getEmailNotifications())
                .lowStockAlerts(d.getLowStockAlerts())
                .lowStockThreshold(d.getLowStockThreshold())
                .allowNegativeStock(d.getAllowNegativeStock())
                .exchangeRateUSD(d.getExchangeRateUSD())
                .exchangeRateEUR(d.getExchangeRateEUR())
                .yapeQrUrl(d.getYapeQrUrl())
                .plinQrUrl(d.getPlinQrUrl())
                .yapePhone(d.getYapePhone())
                .plinPhone(d.getPlinPhone())
                .moduleSizes(d.getModuleSizes())
                .moduleColors(d.getModuleColors())
                .moduleBrand(d.getModuleBrand())
                .moduleExpiration(d.getModuleExpiration())
                .moduleSerial(d.getModuleSerial())
                .moduleWarranty(d.getModuleWarranty())
                .build();
    }
}
