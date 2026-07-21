package ru.pulsecore.admin_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.admin_service.api.dto.UpdatePricesRequest;
import ru.pulsecore.shared.config.constants.feighn.FeignClientConstants;
import ru.pulsecore.shared.config.constants.feighn.FeignPaymentApi;
import ru.pulsecore.shared.dto.payment.PricesResponse;

@FeignClient(
        name = FeignClientConstants.PAYMENT_SERVICE
)
public interface PaymentClient {
//todo добавить обработку всем клиентам

    @PutMapping(FeignPaymentApi.BASE + FeignPaymentApi.UPDATE_PRICE)
    void updatePrice(@RequestBody UpdatePricesRequest request);

    @GetMapping(FeignPaymentApi.BASE + FeignPaymentApi.PRICES)
    PricesResponse getPrices();



}
