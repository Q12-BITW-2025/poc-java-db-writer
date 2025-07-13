package com.poc.java.repo;

import com.poc.java.model.Trade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {


    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE TB_TRADE RESTART IDENTITY", nativeQuery = true)
    void truncateTable();

}
