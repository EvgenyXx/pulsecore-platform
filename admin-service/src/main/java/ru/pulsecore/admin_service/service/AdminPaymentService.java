package ru.pulsecore.admin_service.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.admin_service.api.dto.UpdatePricesRequest;
import ru.pulsecore.admin_service.client.PaymentClient;
import ru.pulsecore.shared.dto.payment.PricesResponse;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {

    private final PaymentClient paymentClient;


    public void updatePrice(@RequestBody UpdatePricesRequest request) {
        paymentClient.updatePrice(request);
    }


    public PricesResponse getPrice(){
        return paymentClient.getPrices();
    }
}
