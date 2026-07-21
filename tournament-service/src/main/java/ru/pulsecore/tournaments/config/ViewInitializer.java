package ru.pulsecore.tournaments.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewInitializer implements CommandLineRunner {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Создание top_players_view...");
        entityManager.createNativeQuery("DROP VIEW IF EXISTS top_players_view CASCADE").executeUpdate();
        entityManager.createNativeQuery("""
            CREATE VIEW top_players_view AS
            SELECT p.id AS player_id, p.name, p.primary_league, 'WEEK' AS period,
                COALESCE(SUM(CASE WHEN tr.date >= DATE_TRUNC('week', CURRENT_DATE)::date THEN tr.amount ELSE 0 END), 0) AS total,
                COUNT(CASE WHEN tr.date >= DATE_TRUNC('week', CURRENT_DATE)::date THEN 1 ELSE NULL END) AS tournaments
            FROM players p LEFT JOIN tournament_results tr ON tr.player_id = p.id
            WHERE p.primary_league IS NOT NULL
            GROUP BY p.id, p.name, p.primary_league
            UNION ALL
            SELECT p.id, p.name, p.primary_league, 'MONTH',
                COALESCE(SUM(CASE WHEN tr.date >= DATE_TRUNC('month', CURRENT_DATE)::date THEN tr.amount ELSE 0 END), 0),
                COUNT(CASE WHEN tr.date >= DATE_TRUNC('month', CURRENT_DATE)::date THEN 1 ELSE NULL END)
            FROM players p LEFT JOIN tournament_results tr ON tr.player_id = p.id
            WHERE p.primary_league IS NOT NULL
            GROUP BY p.id, p.name, p.primary_league
            UNION ALL
            SELECT p.id, p.name, p.primary_league, 'YEAR',
                COALESCE(SUM(CASE WHEN tr.date >= DATE_TRUNC('year', CURRENT_DATE)::date THEN tr.amount ELSE 0 END), 0),
                COUNT(CASE WHEN tr.date >= DATE_TRUNC('year', CURRENT_DATE)::date THEN 1 ELSE NULL END)
            FROM players p LEFT JOIN tournament_results tr ON tr.player_id = p.id
            WHERE p.primary_league IS NOT NULL
            GROUP BY p.id, p.name, p.primary_league
            ORDER BY period, total DESC
        """).executeUpdate();
        log.info("top_players_view создан");
    }
}