package com.finance.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FinanceScheduler {

    @Scheduled(fixedRate = 60000)
    public void runScheduledTask() {
        log.info("Finance scheduler executed successfully");
    }
}