package ru.pulsecore.tournaments.service.extraction;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import ru.pulsecore.tournaments.domain.LeagueType;
import ru.pulsecore.tournaments.domain.Match;
import ru.pulsecore.tournaments.service.parser.LeagueDetector;
import ru.pulsecore.tournaments.service.calculation.NightBonusService;
import ru.pulsecore.tournaments.domain.RemovedResult;
import ru.pulsecore.tournaments.domain.TournamentContext;
import ru.pulsecore.tournaments.domain.TournamentStatus;
import ru.pulsecore.tournaments.service.parser.MatchParser;
import ru.pulsecore.tournaments.service.parser.TournamentParser;
import ru.pulsecore.tournaments.service.parser.TournamentStatusParser;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TournamentExtractor {

    private final TournamentParser tournamentParser;
    private final MatchParser matchParser;
    private final LeagueDetector leagueDetector;
    private final NightBonusService nightBonusService;
    private final TournamentStatusParser tournamentStatusParser;
    private final RemovedPlayerDetector removedPlayerDetector;

    public TournamentContext extract(Document doc) {

        Long tournamentId = tournamentParser.parseTournamentId(doc);
        TournamentStatus status = tournamentStatusParser.parseStatus(doc);
        String date = tournamentParser.parseDate(doc);

        List<Match> matches = matchParser.parseMatches(doc);

        LeagueType league = leagueDetector.detectLeague(doc);
        double nightBonus = nightBonusService.calculateBonus(doc, league.name());

        String removedPlayer = tournamentParser.findRemovedPlayer(doc);
        String time = tournamentParser.parseTime(doc);


        RemovedResult playerDetector = removedPlayerDetector.detect(removedPlayer,matches);



        return new TournamentContext(
                tournamentId,
                status,
                date,
                matches,
                league,
                nightBonus,
                playerDetector.stage(),
                playerDetector.player(),
                time
        );
    }


}