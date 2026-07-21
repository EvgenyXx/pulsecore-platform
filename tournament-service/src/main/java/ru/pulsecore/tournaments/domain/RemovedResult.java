package ru.pulsecore.tournaments.domain;

public record RemovedResult(
        String player,
        RemovedStage stage
) {}