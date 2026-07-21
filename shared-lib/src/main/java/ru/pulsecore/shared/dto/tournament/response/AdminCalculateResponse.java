package ru.pulsecore.shared.dto.tournament.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCalculateResponse {
    private String playerName;
    private String startDate;
    private String endDate;
    private double totalAmount;
    private int tournamentCount;
    private List<TournamentResultItem> tournaments;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TournamentResultItem {
        private String date;
        private double amount;
        private String tournamentTitle;
        private Long tournamentId;
        private boolean hasRemoved;
    }
}