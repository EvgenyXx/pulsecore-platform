package ru.pulsecore.shared.config.constants.feighn;

public final class FeignPaymentApi {
    public static final String BASE = "/api/internal/payment";
    public static final String PRICES = "/prices";
    public static final String UPDATE_PRICE = "/update" ;

    public static final String GRANT_ROLE = "/{playerId}/roles/grant";
    public static final String REVOKE_ROLE = "/{playerId}/roles/revoke";
    public static final String GET_ROLES = "/{playerId}/roles";

    private FeignPaymentApi() {}
}