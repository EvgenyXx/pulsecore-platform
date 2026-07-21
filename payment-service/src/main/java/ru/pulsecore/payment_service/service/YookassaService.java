package ru.pulsecore.payment_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.pulsecore.payment_service.api.dto.internal.Amount;
import ru.pulsecore.payment_service.api.dto.internal.Confirmation;
import ru.pulsecore.payment_service.api.dto.reqauest.YookassaPaymentRequest;
import ru.pulsecore.payment_service.api.dto.response.PaymentResponse;
import ru.pulsecore.payment_service.api.dto.response.YookassaPaymentResponse;
import ru.pulsecore.payment_service.exception.PaymentException;
import ru.pulsecore.payment_service.properties.YookassaProperties;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class YookassaService {

    private final YookassaProperties props;
    private final PriceService priceService;
    private final RestTemplate restTemplate;

    public PaymentResponse createPayment(UUID playerId, int months) {
        int amount = priceService.getPrice(months);
        YookassaPaymentRequest request = buildRequest(playerId, months, amount);
        YookassaPaymentResponse response = executeRequest(request);
        return new PaymentResponse(response.confirmation().confirmationUrl(), response.id());
    }

    private YookassaPaymentRequest buildRequest(UUID playerId, int months, int amount) {
        return new YookassaPaymentRequest(
                new Amount(String.valueOf(amount), props.getCurrency()),
                new Confirmation("redirect", props.getReturnUrl()),
                "Подписка PulseCore на " + months + " мес.",
                Map.of("playerId", playerId.toString(), "months", String.valueOf(months)),
                true
        );
    }

    private YookassaPaymentResponse executeRequest(YookassaPaymentRequest request) {
        try {
            return restTemplate.exchange(
                    props.getYookassaApiUrl(),
                    HttpMethod.POST,
                    new HttpEntity<>(request, createHeaders()),
                    YookassaPaymentResponse.class
            ).getBody();
        } catch (Exception e) {
            log.error("YooKassa API request failed", e);
            throw new PaymentException("Не удалось создать платёж");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Idempotence-Key", UUID.randomUUID().toString());
        headers.setBasicAuth(String.valueOf(props.getShopId()), props.getSecretKey());
        return headers;
    }
}