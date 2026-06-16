package com.tfg.viajes.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tfg.viajes.entities.Viaje;
import com.tfg.viajes.repository.ViajeRepository;
import com.tfg.viajes.services.ClimaService;

@RestController
@RequestMapping("/api/viajes")
public class ClimaController {

	@Autowired
	private ClimaService climaService;

	@Autowired
	private ViajeRepository viajeRepository;

	// GET /api/viajes/{id}/clima -> coordenadas del destino + previsión del tiempo
	@GetMapping("/{id}/clima")
	public ResponseEntity<?> obtenerClima(@PathVariable Long id) {
		Optional<Viaje> viaje = viajeRepository.findById(id);
		if (viaje.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Map<String, Object> clima = climaService.obtenerClimaDeViaje(viaje.get());
		return ResponseEntity.ok(clima);
	}
}