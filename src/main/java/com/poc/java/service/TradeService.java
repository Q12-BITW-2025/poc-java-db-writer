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
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;

    private final CircularBuffer<Trade> tradeBuffer;

    public TradeService(TradeRepository tradeRepository, CircularBuffer<Trade> tradeBuffer) {
        this.tradeRepository = tradeRepository;
        this.tradeBuffer = tradeBuffer;
        this.init();
    }

    public void init() {
        log.info("TradeService Starting worker thread");
        Thread worker = new Thread(this::saveTrade, "trade-processor-thread");
        worker.setDaemon(true);
        worker.start();
        log.info("Worker thread started");
    }

    @SneakyThrows
    void saveTrade() {
        while (true) {
            Trade trade = tradeBuffer.get();
            log.info("Got {} from buffer", trade);
            if (trade != null) {
                Trade savedTrade = tradeRepository.save(trade);
                log.info("Saved as {}", savedTrade);
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
