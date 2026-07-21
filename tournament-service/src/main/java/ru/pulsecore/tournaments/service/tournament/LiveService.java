package ru.pulsecore.tournaments.service.tournament;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.persistence.repository.LineupRepository;
import ru.pulsecore.tournaments.api.dto.response.TournamentLiveDto;
import ru.pulsecore.tournaments.domain.LiveStatus;
import ru.pulsecore.tournaments.mapper.LineupLiveMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LiveService {

    private final LineupRepository lineupRepository;
    private final LineupLiveMapper mapper;
    private final ChatWebSocketService  chatWebSocketService;
    private final PlayerClient playerClient;

    private static final int TOURNAMENT_MAX_DURATION_HOURS = 6;


    public String getLiveSelectedHalls(UUID playerId) {
        return playerClient.getLiveSelectedHalls(playerId);
    }

    public List<TournamentLiveDto> getLive() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        return lineupRepository.findByDate(today)
                .stream()
                .map(l -> {
                    TournamentLiveDto dto = mapper.toDto(l);
                    LocalTime startTime = LocalTime.parse(l.getTime());
                    dto.setStatus(calculateStatus(startTime, now));
                    return dto;
                })
                .toList();
    }

    public Map<Long, Long> getOnlineCounts() {
        return chatWebSocketService.getAllOnlineCounts();
    }



    private LiveStatus calculateStatus(LocalTime startTime, LocalTime now) {
        if (startTime.isAfter(now)) {
            return LiveStatus.UPCOMING;
        }

        LocalTime endTime = startTime.plusHours(TOURNAMENT_MAX_DURATION_HOURS);

        if (endTime.isBefore(startTime) || endTime.isAfter(now)) {
            return LiveStatus.LIVE;
        }

        return LiveStatus.FINISHED;
    }
}