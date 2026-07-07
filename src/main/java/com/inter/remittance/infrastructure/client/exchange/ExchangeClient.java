package com.inter.remittance.infrastructure.client.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "exchange-api",
        url = "${client.apis.exchange-api.url}"
)
public interface ExchangeClient {

    @GetMapping
    ExchangeResponse getExchangeRateForUSD();
}
