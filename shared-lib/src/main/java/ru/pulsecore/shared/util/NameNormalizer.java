package ru.pulsecore.shared.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class NameNormalizer {

    public String normalize(String name) {
        if (name == null || name.isBlank()) return "";
        return name
                .replace("\u00A0", " ")
                .replaceAll("\\(.*?\\)", "")
                .trim()
                .replaceAll("\\s+", " ");
    }

    public List<String> normalizePlayers(List<String> players) {
        return players.stream()
                .map(NameNormalizer::normalize)
                .toList();
    }

    public String normalizeForSearch(String name) {
        if (name == null || name.isBlank()) return "";
        return normalize(name).toLowerCase();
    }
}