package com.inter.remittance.application.cronjob;

import com.inter.remittance.domain.entities.DailyTransactionLimit;
import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.repositories.DailyTransactionLimitRepository;
import com.inter.remittance.domain.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("scheduler")
public class ReestablishPersonDailyTransactionLimit {

    private final PersonRepository personRepository;
    private final DailyTransactionLimitRepository dailyTransactionLimitRepository;


    public ReestablishPersonDailyTransactionLimit(
            PersonRepository personRepository,
            DailyTransactionLimitRepository dailyTransactionLimitRepository
            ) {
        this.personRepository = personRepository;
        this.dailyTransactionLimitRepository = dailyTransactionLimitRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ReestablishPersonDailyTransactionLimit.class);

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    void execute() {

        int page = 0;
        PageResult<Person> persons;

        do {

            persons = personRepository.findAll(page, 100);

            persons.content()
                    .parallelStream()
                    .forEach(this::resetLimit);
            page++;

        } while (page < persons.totalPages());
    }


    private void resetLimit(Person person) {

        try {
            log.info(
                    "Reestablishing daily transaction limit for person {}",
                    person.document().value()
            );

            DailyTransactionLimit.setInitialDailyTransactionLimit(
                    person.dailyTransactionLimits(),
                    person.document().type()
            ).forEach(dailyTransactionLimitRepository::update);

        } catch (Exception e) {

            log.error(
                    "Error reestablishing daily transaction limit for person {}",
                    person.document().value(),
                    e
            );
        }
    }
}
