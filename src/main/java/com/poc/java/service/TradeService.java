package com.poc.java.service;

import com.poc.java.buffer.CircularBuffer;
import com.poc.java.model.Trade;
import com.poc.java.repo.TradeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;

    private final CircularBuffer<Trade> tradeBuffer;

    /**
     * This method starts automatically after Spring initializes the bean.
     * It loops forever, polling the ring buffer and saving to the DB.
     */
    @PostConstruct
    public void init() {
        Thread worker = new Thread(this::saveTrade, "trade-processor-thread");
        worker.setDaemon(true);
        worker.start();
    }

    @SneakyThrows
    void saveTrade() {
        while (true) {
            Trade trade = tradeBuffer.get();
            if (trade != null) {
                tradeRepository.save(trade);
            }
            Thread.sleep(50);
        }
    }

    @Scheduled(cron = "0 0/30 * * * *")
    void truncateTable() {
        log.info("Running truncate table for Trade Repository");
        tradeRepository.truncateTable();
    }

}
