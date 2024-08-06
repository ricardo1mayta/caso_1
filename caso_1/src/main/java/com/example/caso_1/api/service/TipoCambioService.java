package com.example.caso_1.api.service;

import com.example.caso_1.api.model.TipoCambio;
import com.example.caso_1.api.repository.TipoCambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TipoCambioService {

    @Autowired
    private TipoCambioRepository tipoCambioRepository;

    public Flux<TipoCambio> getAllRates() {
        return tipoCambioRepository.findAll();
    }

    public Mono<TipoCambio> getRateById(Long id) {
        return tipoCambioRepository.findById(id);
    }

    public Mono<TipoCambio> saveRate(TipoCambio tipoCambio) {
        return tipoCambioRepository.save(tipoCambio);
    }

    public Mono<Void> deleteRate(Long id) {
        return tipoCambioRepository.deleteById(id);
    }
}