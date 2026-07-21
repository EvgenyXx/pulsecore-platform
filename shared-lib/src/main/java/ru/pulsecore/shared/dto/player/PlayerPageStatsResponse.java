package ru.pulsecore.shared.dto.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPageStatsResponse {
    private String name;
    private String paths;
    private Long total;
    private Double percent;
}