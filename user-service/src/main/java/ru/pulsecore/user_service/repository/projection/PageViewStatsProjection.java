package ru.pulsecore.user_service.repository.projection;

public interface PageViewStatsProjection {
    String getPath();
    String getMethod();
    long getCount();
}