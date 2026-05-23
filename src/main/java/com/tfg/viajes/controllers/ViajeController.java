package com.tfg.viajes.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tfg.viajes.entities.Viaje;
import com.tfg.viajes.services.ViajeService;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    // GET /viajes/usuario/{usuarioId} → viajes de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Viaje> obtenerViajesDeUsuario(@PathVariable Long usuarioId) {
        return viajeService.obtenerViajesDeUsuario(usuarioId);
    }

    // GET /viajes/{id} → obtener un viaje por id
    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        Optional<Viaje> viaje = viajeService.obtenerPorId(id);
        return viaje.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // POST /viajes/{creadorId} → crear un viaje nuevo
    @PostMapping("/{creadorId}")
    public ResponseEntity<?> crearViaje(@RequestBody Viaje viaje,
                                        @PathVariable Long creadorId) {
        try {
            Viaje nuevo = viajeService.crearViaje(viaje, creadorId);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /viajes/{viajeId}/participantes/{usuarioId} → añadir participante
    @PostMapping("/{viajeId}/participantes")
    public ResponseEntity<?> anyadirParticipante(@PathVariable Long viajeId,
                                                  @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("id").toString());
        try {
            return ResponseEntity.ok(viajeService.anyadirParticipante(viajeId, usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}