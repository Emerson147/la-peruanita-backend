package com.emersondev.application.usecase.configuracion;

import com.emersondev.domain.model.Configuracion;
import com.emersondev.domain.repository.ConfiguracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObtenerConfiguracionUseCase {

    private final ConfiguracionRepository configuracionRepository;

    public Configuracion ejecutar() {
        return configuracionRepository.obtenerConfiguracionGlobal()
                .orElseGet(() -> {
                    Configuracion defaultConfig = Configuracion.builder()
                            .id(UUID.randomUUID())
                            .businessName("Importaciones DenRaf")
                            .currency("PEN")
                            .taxPercent(18.0)
                            .autoPrint(false)
                            .emailNotifications(false)
                            .lowStockAlerts(true)
                            .lowStockThreshold(5)
                            .allowNegativeStock(false)
                            .exchangeRateUSD(3.75)
                            .exchangeRateEUR(4.10)
                            .ticketFooterMessage("¡Gracias por su preferencia! No se aceptan devoluciones después de 7 días.")
                            .moduleSizes(true)
                            .moduleColors(true)
                            .moduleBrand(true)
                            .moduleExpiration(false)
                            .moduleSerial(false)
                            .moduleWarranty(false)
                            .build();
                    return configuracionRepository.save(defaultConfig);
                });
    }
}
