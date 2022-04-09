package com.joshcode.fraud;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class FraudCheckHistoryService {

    private final FraudCheckHistoryRepository fraudCheckHistoryRepository;


    public boolean isFraudulentCustomer(Integer customerId){
        fraudCheckHistoryRepository.save(
                FraudCheckHistory
                        .builder()
                        .isFraudster(false)
                        .customerId(customerId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        return false;
    }
}
