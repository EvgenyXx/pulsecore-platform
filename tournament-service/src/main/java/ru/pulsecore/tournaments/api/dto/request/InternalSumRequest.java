package ru.pulsecore.tournaments.api.dto.request;

import java.time.LocalDate;

public record InternalSumRequest(LocalDate start, LocalDate end, int page, int size) {
}
