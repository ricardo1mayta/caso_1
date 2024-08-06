package com.example.caso_1.api.repository;

import com.example.caso_1.api.model.TipoCambio;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TipoCambioRepository extends ReactiveCrudRepository<TipoCambio, Long> {
}
