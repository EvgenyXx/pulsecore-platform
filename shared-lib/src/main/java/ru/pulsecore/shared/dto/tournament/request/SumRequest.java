package ru.pulsecore.shared.dto.tournament.request;

import java.time.LocalDate;
import java.util.UUID;

public record SumRequest(UUID playerId, LocalDate start, LocalDate end, int page, int size) {}