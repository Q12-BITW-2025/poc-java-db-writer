package com.poc.java.service;

import com.poc.java.buffer.CircularBuffer;
import com.poc.java.model.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final CircularBuffer<Trade> tradeBuffer;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "trade", groupId = "db-writer", concurrency = "3")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message: key={}, value={}, partition={}, offset={}",
                record.key(), record.value(), record.partition(), record.offset());
        try {
            Trade trade = objectMapper.readValue(record.value(), Trade.class);
            tradeBuffer.add(trade);
            log.info("Added {} to buffer", trade);
        } catch (Exception e) {
            log.error("Failed to deserialize Kafka message: {}", e.getMessage(), e);
        }

    }
}
