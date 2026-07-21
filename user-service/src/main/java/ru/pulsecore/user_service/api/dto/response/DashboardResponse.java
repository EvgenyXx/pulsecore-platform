package ru.pulsecore.user_service.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pulsecore.shared.dto.tournament.response.LastResultDto;
import ru.pulsecore.shared.dto.tournament.response.UpcomingLineupDto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse implements Serializable {
    private String playerName;
    private LastResultDto lastResult;
    private List<UpcomingLineupDto> upcomingLineups;
    private SubscriptionInfoDto subscription;
    private String primaryLeague;

    public static DashboardResponse empty() {
        return DashboardResponse.builder()
                .playerName("Неизвестный игрок")
                .upcomingLineups(Collections.emptyList())
                .build();
    }
}