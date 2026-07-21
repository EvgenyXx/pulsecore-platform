package ru.pulsecore.shared.config.constants.feighn;

public final class FeignPlayerApi {
    public static final String BASE = "/api/internal/players";

    public static final String GET_BY_ID = "/{playerId}";
    public static final String SETTINGS = "/{playerId}/settings";
    public static final String SEARCH = "/search";
    public static final String DELETE = "/{playerId}";
    public static final String ACTIVATE_SUBSCRIPTION = "/{playerId}/subscription/activate";
    public static final String DEACTIVATE_SUBSCRIPTION = "/{playerId}/subscription/deactivate";
    public static final String BROADCAST_RECIPIENTS = "/broadcast-recipients";
    public static final String ID_BY_NAME = "/by-name/{name}";
    public static final String ALL_ACTIVE = "";
    public static final String GET_NAME_BY_ID = "/{playerId}/name";


    public static final String PAGE_VIEWS_STATS = "/page-views/stats";
    public static final String PAGE_VIEWS_PLAYER_STATS = "/page-views/player-stats";
    public static final String GET_SUBSCRIPTION = "/{playerId}/subscription";


    public static final String GET_HALLS = "/{playerId}/halls";
    public static final String GET_LIVE_HALLS = "/{playerId}/live-halls";


    public static final String GRANT_ROLE = "/{playerId}/roles/grant";
    public static final String REVOKE_ROLE = "/{playerId}/roles/revoke";
    public static final String GET_ROLES = "/{playerId}/roles";



    private FeignPlayerApi() {}
}