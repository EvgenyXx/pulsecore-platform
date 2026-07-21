package ru.pulsecore.shared.util;

import feign.FeignException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class FeignUtils {

    public static <T> Optional<T> tryExecute(Supplier<T> supplier, String serviceName) {
        try {
            return Optional.of(supplier.get());
        } catch (FeignException.ServiceUnavailable e) {
            log.warn("{} недоступен", serviceName);
            return Optional.empty();
        } catch (FeignException e) {
            log.error("{} ошибка: {}", serviceName, e.getMessage());
            return Optional.empty();
        }
    }
}