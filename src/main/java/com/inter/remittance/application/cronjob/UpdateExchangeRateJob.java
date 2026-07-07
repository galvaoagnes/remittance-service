package com.inter.remittance.application.cronjob;

import com.inter.remittance.domain.enums.Currency;
import com.inter.remittance.application.service.UpdateExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("scheduler")
public class UpdateExchangeRateJob {

    private final UpdateExchangeRateService updateExchangeRateService;

    public UpdateExchangeRateJob(UpdateExchangeRateService updateExchangeRateService) {
        this.updateExchangeRateService = updateExchangeRateService;
    }

    private static final Logger log = LoggerFactory.getLogger(UpdateExchangeRateJob.class);

    @Scheduled(cron = "0 1 0 * * 1-5", zone = "America/Sao_Paulo")
    void execute() {
        log.info("Updating exchange rate.");
        for (Currency currency : Currency.values()) {
            if (currency.hasExchangeRate()){
                updateExchangeRateService.execute(currency);
                log.info("Exchange rate for {} updated successfully.", currency);
            }
        }
    }
}
