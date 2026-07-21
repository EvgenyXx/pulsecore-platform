package ru.pulsecore.payment_service.api.controller.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsecore.payment_service.api.dto.reqauest.WebhookRequest;
import ru.pulsecore.payment_service.service.WebhookService;

import static ru.pulsecore.payment_service.api.YookassaApi.WEBHOOK;

@Tag(name = "Платежи")
@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @Operation(summary = "Обработать вебхук от YooKassa")
    @PostMapping(WEBHOOK)
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookRequest webhookRequest) {
        log.info("Webhook received");
        webhookService.process(webhookRequest);
        return ResponseEntity.ok("ok");
    }
}