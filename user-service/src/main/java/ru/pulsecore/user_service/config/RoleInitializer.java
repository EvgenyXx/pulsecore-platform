package ru.pulsecore.user_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {

            // 1. Вставляем роли
            try (PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO roles (name)
                    VALUES (?)
                    ON CONFLICT (name) DO NOTHING
            """)) {
                ps.setString(1, "ROLE_USER");
                ps.executeUpdate();

                ps.setString(1, "ROLE_ADMIN");
                ps.executeUpdate();
            }

            // 2. Выдаём ROLE_USER всем, у кого ещё нет
            try (Statement stmt = conn.createStatement()) {
                int updated = stmt.executeUpdate("""
                    INSERT INTO player_roles (player_id, role_id)
                    SELECT p.id, r.id
                    FROM players p, roles r
                    WHERE r.name = 'ROLE_USER'
                      AND NOT EXISTS (
                        SELECT 1 FROM player_roles pr
                        WHERE pr.player_id = p.id AND pr.role_id = r.id
                    )
                """);
                if (updated > 0) {
                    log.info("Выдана роль ROLE_USER {} игрокам", updated);
                }
            }

            // 3. ROLE_ADMIN конкретному email
            try (PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO player_roles (player_id, role_id)
                    SELECT p.id, r.id
                    FROM players p, roles r
                    WHERE p.email = ? AND r.name = 'ROLE_ADMIN'
                      AND NOT EXISTS (
                        SELECT 1 FROM player_roles pr
                        WHERE pr.player_id = p.id AND pr.role_id = r.id
                    )
            """)) {
                ps.setString(1, "evgenypavlov666@yandex.ru");
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    log.info("Выдана роль ROLE_ADMIN админу");
                }
            }

            // 4. Проверяем результат
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name FROM roles")) {
                log.info("Роли в БД:");
                while (rs.next()) {
                    log.info("  - {}", rs.getString("name"));
                }
            }
        }
    }
}