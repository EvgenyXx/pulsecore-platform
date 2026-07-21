package ru.pulsecore.tournaments.domain;

public enum ChatEventType {
    DELETE,
    EDIT;

    public String asType() {
        return name();
    }
}