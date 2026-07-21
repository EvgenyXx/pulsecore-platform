package ru.pulsecore.tournaments.service.lineup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.tournaments.api.dto.response.LineupDto;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.persistence.entity.Lineup;
import ru.pulsecore.tournaments.persistence.repository.LineupRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LineupFacade {

    private final LineupService lineupService;
    private final LineupRepository lineupRepository;
    private final PlayerClient playerClient;



    public Map<String, List<LineupDto>> getAllGroupedByHall(LocalDate date) {
        List<Lineup> all = lineupRepository.findByDate(date);
        return groupByHall(all.stream().map(this::toDto).toList());
    }

    public Map<String, List<LineupDto>> getMyGroupedByHall(UUID playerId, LocalDate date) {
        String hallsStr = playerClient.getSelectedHalls(playerId);
        if (hallsStr == null || hallsStr.isBlank()) {
            return Map.of();
        }

        String playerName = playerClient.getPlayerName(playerId);
        List<String> halls = Arrays.asList(hallsStr.split(",\\s*"));
        List<Lineup> filtered = lineupService.getLineupsForHalls(date, halls);

        List<LineupDto> dtos = filtered.stream()
                .map(this::toDto)
                .map(dto -> markPlayer(dto, playerName))
                .toList();

        return groupByHall(dtos);
    }

    private LineupDto markPlayer(LineupDto dto, String playerName) {
        boolean isPlayer = dto.getPlayers() != null &&
                Arrays.stream(dto.getPlayers().split(","))
                        .map(String::trim)
                        .anyMatch(p -> p.equalsIgnoreCase(playerName));
        dto.setPlayer(isPlayer);
        return dto;
    }

    private LineupDto toDto(Lineup lineup) {
        return LineupDto.builder()
                .time(lineup.getTime())
                .league(lineup.getLeague())
                .hall(lineup.getHall())
                .players(lineup.getPlayers())
                .date(lineup.getDate().toString())
                .build();
    }

    private Map<String, List<LineupDto>> groupByHall(List<LineupDto> lineups) {
        return lineups.stream()
                .collect(Collectors.groupingBy(
                        l -> l.getHall() != null ? l.getHall() : "Без зала",
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}