package ru.pulsecore.admin_service.api;

public final class AdminApi {

    private AdminApi() {}

    public static final String BASE = "/api/admin";
    public static final String PRICES = "/prices";
    public static final String ROLES_GRANT = "/players/{playerId}/roles/grant";
    public static final String ROLES_REVOKE = "/players/{playerId}/roles/revoke";
    public static final String ROLES = "/players/{playerId}/roles";

    public static final String DELETE_PLAYER = "/players/{playerId}";

    public static final String SUBSCRIBE = "/players/{playerId}/subscribe";
    public static final String UNSUBSCRIBE = "/players/{playerId}/unsubscribe";

    public static final String TOURNAMENT_CALCULATE = "/tournaments/calculate";

    public static final String PLAYER_SUBSCRIPTION = "/players/{playerId}/subscription";

    public static final String PLAYER_TOURNAMENTS = "/players/{playerId}/tournaments";
    public static final String PLAYER_TOURNAMENTS_RESYNC = "/players/{playerId}/tournaments/resync";

    public static final String BROADCAST = "/broadcast";

    public static final String PAGE_VIEWS_STATS = "/stats/page-views";
    public static final String PAGE_VIEWS_PLAYERS = "/stats/page-views/players";

    public static final String SEARCH = "/players/search";
}