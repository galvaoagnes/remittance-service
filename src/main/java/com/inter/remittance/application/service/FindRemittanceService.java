package com.inter.remittance.application.service;

import com.inter.remittance.domain.entities.Remittance;
import com.inter.remittance.domain.repositories.RemittanceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindRemittanceService {

    private final RemittanceRepository remittanceRepository;

    public FindRemittanceService(RemittanceRepository remittanceRepository) {
        this.remittanceRepository = remittanceRepository;
    }

    public Remittance findById(UUID id){
        return remittanceRepository.findById(id);
    }

}
