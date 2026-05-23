package com.tfg.viajes.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tfg.viajes.entities.Gasto;
import com.tfg.viajes.services.GastoService;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    // GET /gastos/viaje/{viajeId} → gastos de un viaje
    @GetMapping("/viaje/{viajeId}")
    public List<Gasto> obtenerGastosDeViaje(@PathVariable Long viajeId) {
        return gastoService.obtenerGastosDeViaje(viajeId);
    }

    // GET /gastos/usuario/{pagadorId} → gastos de un usuario
    @GetMapping("/usuario/{pagadorId}")
    public List<Gasto> obtenerGastosDeUsuario(@PathVariable Long pagadorId) {
        return gastoService.obtenerGastosDeUsuario(pagadorId);
    }

    // POST /gastos/{pagadorId}/{viajeId} → registrar un gasto nuevo
    @PostMapping("/{pagadorId}/{viajeId}")
    public ResponseEntity<?> registrarGasto(@RequestBody Gasto gasto,
                                             @PathVariable Long pagadorId,
                                             @PathVariable Long viajeId) {
        try {
            Gasto nuevo = gastoService.registrarGasto(gasto, pagadorId, viajeId);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> registrarGasto(@RequestBody Map<String, Object> body) {
        Long pagadorId = Long.valueOf(body.get("pagadorId").toString());
        Long viajeId = Long.valueOf(body.get("viajeId").toString());
        Gasto gasto = new Gasto();
        gasto.setDescripcion(body.get("descripcion").toString());
        gasto.setCantidad(Double.valueOf(body.get("cantidad").toString()));
        try {
            return ResponseEntity.ok(gastoService.registrarGasto(gasto, pagadorId, viajeId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /gastos/{id} → eliminar un gasto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarGasto(@PathVariable Long id) {
        try {
            gastoService.eliminarGasto(id);
            return ResponseEntity.ok("Gasto eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}