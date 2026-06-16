package com.tfg.viajes.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tfg.viajes.entities.Actividad;
import com.tfg.viajes.services.ActividadService;

@RestController
@RequestMapping("/api/itinerario")
public class ActividadController {

	@Autowired
	private ActividadService actividadService;

	// GET /api/itinerario/viaje/{viajeId} -> itinerario completo de un viaje
	@GetMapping("/viaje/{viajeId}")
	public List<Actividad> obtenerItinerario(@PathVariable Long viajeId) {
		return actividadService.obtenerItinerarioDeViaje(viajeId);
	}

	// POST /api/itinerario/{viajeId} -> añadir una actividad al itinerario
	@PostMapping("/{viajeId}")
	public ResponseEntity<?> crearActividad(@RequestBody Actividad actividad, @PathVariable Long viajeId) {
		try {
			return ResponseEntity.ok(actividadService.crearActividad(actividad, viajeId));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// PUT /api/itinerario/{id} -> editar una actividad
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarActividad(@PathVariable Long id, @RequestBody Actividad actividad) {
		try {
			return ResponseEntity.ok(actividadService.actualizarActividad(id, actividad));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// DELETE /api/itinerario/{id} -> eliminar una actividad
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarActividad(@PathVariable Long id) {
	    try {
	        actividadService.eliminarActividad(id);
	        return ResponseEntity.ok().body(Map.of("success", true));
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(Map.of(
	            "success", false,
	            "error", e.getMessage()
	        ));
	    }
	}
}