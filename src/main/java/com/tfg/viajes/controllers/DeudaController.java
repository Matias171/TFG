package com.tfg.viajes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tfg.viajes.entities.Deuda;
import com.tfg.viajes.services.DeudaService;

@RestController
@RequestMapping("/api/deudas")
public class DeudaController {

    @Autowired
    private DeudaService deudaService;

    // GET /deudas/viaje/{viajeId} → deudas de un viaje
    @GetMapping("/viaje/{viajeId}")
    public List<Deuda> obtenerDeudasDeViaje(@PathVariable Long viajeId) {
        return deudaService.obtenerDeudasDeViaje(viajeId);
    }

    // POST /deudas/calcular/{viajeId} → calcular y guardar deudas
    @PostMapping("/calcular/{viajeId}")
    public ResponseEntity<?> calcularDeudas(@PathVariable Long viajeId) {
        try {
            List<Deuda> deudas = deudaService.calcularDeudas(viajeId);
            return ResponseEntity.ok(deudas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /deudas/{id}/pagar → marcar deuda como pagada
    @PutMapping("/{id}/pagar")
    public ResponseEntity<?> marcarComoPagada(@PathVariable Long id) {
        try {
            Deuda deuda = deudaService.marcarComoPagada(id);
            return ResponseEntity.ok(deuda);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}