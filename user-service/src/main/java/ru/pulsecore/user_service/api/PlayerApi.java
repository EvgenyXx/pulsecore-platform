package ru.pulsecore.user_service.api;

public final class PlayerApi {
    private PlayerApi() {}

    public static final String BASE_PATH = "/api";

    // Auth
    public static final String LOGIN = "/auth/login";
    public static final String REGISTER = "/auth/register";
    public static final String VERIFY_EMAIL = "/auth/verify-email";
    public static final String VERIFY_PASSWORD = "/auth/verify-password";
    public static final String FORGOT_PASSWORD = "/auth/forgot-password";
    public static final String RESET_PASSWORD = "/auth/reset-password";
    public static final String REFRESH = "/auth/refresh";
    public static final String LOGOUT = "/auth/logout";
    public static final String ME = "/auth/me";
    public static final String ME_THEME = "/auth/me/theme";
    public static final String OAUTH_FINISH = "/auth/oauth-finish";

    // Player
    public static final String PROFILE = "/player/profile";
    public static final String CHANGE_PASSWORD = "/player/change-password";
    public static final String HALLS = "/player/halls";
    public static final String NOTIFICATIONS = "/player/notifications";
    public static final String NOTIFICATIONS_STATUS = "/player/notifications/status";
    public static final String SUBSCRIPTION = "/player/subscription";
    public static final String REPORTS = "/player/reports";
    public static final String REPORTS_PENDING = "/player/reports/pending";
    public static final String REPORTS_CANCEL = "/player/reports/{id}/cancel";
    public static final String PUSH_STATUS = "/player/push-status";
    public static final String PUSH_TOGGLE = "/player/toggle-push";
    public static final String LIVE_HALLS = "/player/live-halls";

    // Online
    public static final String ONLINE = "/online";

    // Shared
    public static final String QR = "/shared/qr";

    // Internal
    public static final String INTERNAL_PLAYERS = "/internal/players";
}