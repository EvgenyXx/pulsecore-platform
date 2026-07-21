package ru.pulsecore.tournaments.mapper;

import org.springframework.stereotype.Component;
import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.tournaments.persistence.entity.Lineup;

import java.time.LocalDate;

@Component
public class LineupMapper {

    public Lineup toEntity(TournamentDto t, LocalDate date, String time) {
        return Lineup.builder()
                .league(t.getLeague())
                .time(time)
                .hall(t.getHall())
                .players(String.join(", ", t.getPlayers()))
                .date(date)
                .build();
    }
}