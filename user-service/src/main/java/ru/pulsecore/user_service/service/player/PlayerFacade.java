package ru.pulsecore.user_service.service.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.user_service.api.dto.request.ChangePasswordRequest;
import ru.pulsecore.user_service.api.dto.request.UpdateProfileRequest;

import ru.pulsecore.user_service.api.dto.response.NotificationsStatusResponse;
import ru.pulsecore.user_service.api.dto.response.PlayerProfileResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerFacade {

    private final PlayerProfileService profileService;
    private final PlayerNotificationService notificationService;
    private final PlayerHallsService hallsService;


    public boolean togglePushEnabled(UUID playerId) {
        return notificationService.togglePushEnabled(playerId);
    }

    public boolean isPushEnabled(UUID playerId) {
        return notificationService.isPushEnabled(playerId);
    }

    public PlayerProfileResponse updateProfile(UUID playerId, UpdateProfileRequest request) {
        return profileService.updateProfile(playerId, request);
    }

    public MessageResponse changePassword(UUID playerId, ChangePasswordRequest request) {
        profileService.changePassword(playerId, request);
        return new MessageResponse("Пароль изменён");
    }


    public void saveLiveSelectedHalls(UUID playerId, String halls) {
        hallsService.saveLiveSelectedHalls(playerId, halls);
    }


    public MessageResponse toggleNotifications(UUID playerId, boolean enabled) {
        notificationService.setNotificationsEnabled(playerId, enabled);
        return new MessageResponse(enabled ? "Уведомления включены" : "Уведомления отключены");
    }

    public NotificationsStatusResponse getNotificationsStatus(UUID playerId) {
        return new NotificationsStatusResponse(notificationService.isNotificationsEnabled(playerId));
    }

    public void saveSelectedHalls(UUID playerId, String halls) {
        hallsService.saveSelectedHalls(playerId, halls);
    }

}