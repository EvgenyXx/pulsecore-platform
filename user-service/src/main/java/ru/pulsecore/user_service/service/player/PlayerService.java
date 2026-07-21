package ru.pulsecore.user_service.service.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.util.NameNormalizer;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.repository.PlayerRepository;
import ru.pulsecore.user_service.exception.player.PlayerNotFoundException;






import java.util.List;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;




    public Player getById(UUID id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Player findById(UUID id) {
        return playerRepository.findById(id).orElse(null);
    }

    public List<Player> getAll() {
        return playerRepository.findAll();
    }






    @Transactional
    public Player save(Player player) {
        if (player.getName() != null) {
            player.setName(NameNormalizer.normalize(player.getName()));
        }
        return playerRepository.save(player);
    }

    @Transactional
    public void deletePlayer(UUID id) {

        playerRepository.deleteById(id);
    }


}