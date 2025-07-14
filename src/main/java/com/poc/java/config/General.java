package com.poc.java.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.java.buffer.CircularBuffer;
import com.poc.java.model.Trade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class General {

    @Bean
    CircularBuffer<Trade> tradeBuffer() {
        return new CircularBuffer<Trade>();
    }

    @Bean
    public ObjectMapper objectMapper() {return new ObjectMapper();}
}
