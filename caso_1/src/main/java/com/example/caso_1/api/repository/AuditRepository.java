package com.example.caso_1.api.repository;

import com.example.caso_1.api.model.Audit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AuditRepository extends ReactiveCrudRepository<Audit, Long> {
}
