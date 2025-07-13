package com.poc.java.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_TRADE")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exchange;

    private String symbol;

    private Long timestamp; // epoch timestamp

    private Double price;

    private Double size;

    private String tradeId; // use camelCase for Java field, maps to snake_case if needed

}
