package ru.pulsecore.tournaments.api.dto.request;

public record UpdateResultRequest(
        Double amount,
        Double bonus,
        Long id) {}