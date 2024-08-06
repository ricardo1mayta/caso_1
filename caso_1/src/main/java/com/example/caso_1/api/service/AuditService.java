package com.example.caso_1.api.service;

import com.example.caso_1.api.model.Audit;
import com.example.caso_1.api.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public Mono<Audit> saveAudit(Audit audit) {
        return auditRepository.save(audit);
    }
}