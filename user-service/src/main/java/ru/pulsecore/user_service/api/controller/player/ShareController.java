package ru.pulsecore.user_service.api.controller.player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.service.qr.QrCodeService;

@Tag(name = "Прочее")
@ApiV1PlayerController
@RequiredArgsConstructor
public class ShareController {

    private final QrCodeService qrCodeService;

    @Operation(summary = "Получить QR-код приложения")
    @GetMapping(PlayerApi.QR)
    public ResponseEntity<byte[]> getQrCode() throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeService.generateQrCode());
    }
}