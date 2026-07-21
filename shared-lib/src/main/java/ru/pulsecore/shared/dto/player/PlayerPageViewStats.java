package ru.pulsecore.shared.dto.player;

import java.util.List;

public record PlayerPageViewStats(
        String name,
        List<PathCount> paths,
        long totalCount,
        double percent
) {
    public record PathCount(String path, long count) {}
}