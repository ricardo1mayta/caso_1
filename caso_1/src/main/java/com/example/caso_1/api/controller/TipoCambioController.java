package com.example.caso_1.api.controller;


import com.example.caso_1.api.model.Audit;
import com.example.caso_1.api.model.TipoCambio;
import com.example.caso_1.api.service.AuditService;
import com.example.caso_1.api.service.TipoCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tipocambio")
public class TipoCambioController {

    @Autowired
    private TipoCambioService tipoCambioService;
    @Autowired
    private AuditService auditService;
    @GetMapping(produces = "application/json")
    public Flux<TipoCambio> getAllRates() {
        return tipoCambioService.getAllRates();
    }

    @GetMapping( "/{id}")
    public Mono<ResponseEntity<TipoCambio>> getRateById(@PathVariable Long id) {
        return tipoCambioService.getRateById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



    @PostMapping(consumes = "application/json", produces = "application/json")
    public Mono<TipoCambio> saveRate(@RequestBody TipoCambio tipoCambio) {
        // Lógica para guardar el tipo de cambio
        return tipoCambioService.saveRate(tipoCambio);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteRate(@PathVariable Long id) {
        return tipoCambioService.deleteRate(id);
    }



    @PostMapping("/intercambio")
    public Mono<ResponseEntity<Double>> exchange(@RequestParam double amount, @RequestParam String fromCurrency, @RequestParam String toCurrency, @RequestParam String username) {
        return tipoCambioService.getAllRates()
                .filter(rate -> rate.getFromCurrency().equals(fromCurrency) && rate.getToCurrency().equals(toCurrency))
                .next()
                .flatMap(rate -> {
                    double exchangedAmount = amount * rate.getRate();
                    Audit audit = new Audit();
                    audit.setUsername(username);
                    audit.setAction("intercambio");
                    audit.setDetails("intercambios " + amount + " " + fromCurrency + " to " + exchangedAmount + " " + toCurrency);
                    return auditService.saveAudit(audit)
                            .thenReturn(ResponseEntity.ok(exchangedAmount));
                });
    }

}