package ru.pulsecore.shared.config.constants.feighn;

public final class FeignTournamentApi {
    public static final String BASE = "/api/internal/tournaments";





    public static final String SUM = "/sum";


    // Admin
    public static final String PLAYER_TOURNAMENTS = "/admin/player/{playerId}/tournaments";
    public static final String PLAYER_TOURNAMENTS_RESYNC = "/admin/player/{playerId}/tournaments/resync";
    public static final String CALCULATE = "/admin/calculate";

    private FeignTournamentApi() {}
}