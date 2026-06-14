package com.emersondev.infrastructure.external;

import com.emersondev.domain.model.PersonaReniec;
import com.emersondev.domain.repository.ConsultasReniecPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

@Slf4j
@Component
public class ReniecRestClientAdapter implements ConsultasReniecPort {

    private final RestTemplate restTemplate;

    @Value("${apisnetpe.url}")
    private String apiUrl;

    @Value("${apisnetpe.token}")
    private String apiToken;

    public ReniecRestClientAdapter() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Optional<PersonaReniec> consultarPorDni(String dni) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = apiUrl + "?numero=" + dni;
            log.info("Consultando RENIEC para DNI: {}", dni);

            ResponseEntity<ApisNetPeResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ApisNetPeResponse.class
            );

            if (response.getBody() != null) {
                ApisNetPeResponse res = response.getBody();
                return Optional.of(new PersonaReniec(
                        res.documentNumber(),
                        res.firstName(),
                        res.firstLastName(),
                        res.secondLastName(),
                        "1" // DNI
                ));
            }

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("DNI {} no encontrado en RENIEC", dni);
        } catch (Exception e) {
            log.error("Error al consultar DNI {}: {}", dni, e.getMessage());
        }
        
        return Optional.empty();
    }

    // DTO interno para mapear la respuesta
    private record ApisNetPeResponse(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("first_last_name") String firstLastName,
            @JsonProperty("second_last_name") String secondLastName,
            @JsonProperty("document_number") String documentNumber
    ) {}
}
