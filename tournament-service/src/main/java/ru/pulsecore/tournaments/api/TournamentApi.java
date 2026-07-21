package ru.pulsecore.tournaments.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TournamentApi {
    public static final String BASE_PATH = "/api/tournaments";

    // Топы
    public static final String TOP_ALL = "/top/{period}";
    public static final String TOP_BY_LEAGUE = "/top/{period}/{league}";

    // Дашборд
    public static final String LAST_RESULT = "/last-result";
    public static final String UPCOMING_LINEUPS = "/upcoming-lineups";


    // Сумма
    public static final String SUM = "/sum";

    // Аналитика
    public static final String ANALYTICS = "/analytics";
    public static final String MONTHLY_INCOME = "/monthly-income";
    public static final String DAILY_INCOME = "/daily-income";
    public static final String BEST_TIME = "/best-time";

    // Live
    public static final String LIVE = "/live";
    public static final String ONLINE_ALL = "/online/all";

    // Лайнапы
    public static final String LINEUPS_ALL = "/lineups/all";
    public static final String LINEUPS_MY = "/lineups/my";
    public static final String LINEUPS_LIVE_HALLS = "/lineups/live-halls";

    // Редактирование
    public static final String UPDATE_RESULT = "/result/{id}";


    // Чат
    public static final String CHAT_LINEUP = "/chat/{lineupId}";
    public static final String CHAT_PLAYERS_SEARCH = "/chat/players/search";
    public static final String CHAT_MESSAGE = "/chat/message/{id}";
    public static final String PARAM_LINEUP_ID = "lineupId";
    public static final String PARAM_MESSAGE_ID = "id";




}