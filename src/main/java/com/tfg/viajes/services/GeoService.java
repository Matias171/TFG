package com.tfg.viajes.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Servicio que convierte un nombre de lugar (ej: "Roma, Italia") en
 * coordenadas (latitud/longitud) usando el servicio gratuito Nominatim
 * de OpenStreetMap.
 */
@Service
public class GeoService {

	@Autowired
	private RestTemplate restTemplate;

	private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

	// Resultado simple: latitud, longitud y nombre que ha devuelto Nominatim
	public static class Coordenadas {
		public double lat;
		public double lon;
		public String nombre;

		public Coordenadas(double lat, double lon, String nombre) {
			this.lat = lat;
			this.lon = lon;
			this.nombre = nombre;
		}
	}

	// Busca las coordenadas de un texto (destino, ciudad, lugar, etc.)
	@SuppressWarnings("unchecked")
	public Optional<Coordenadas> geocodificar(String texto) {
		if (texto == null || texto.isBlank()) {
			return Optional.empty();
		}

		try {
			String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
					.queryParam("q", texto)
					.queryParam("format", "json")
					.queryParam("limit", 1)
					.toUriString();

			// Nominatim exige indicar un User-Agent identificable
			HttpHeaders headers = new HttpHeaders();
			headers.set("User-Agent", "ViajesAppTFG/1.0 (proyecto académico)");
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
			List<Map<String, Object>> resultados = response.getBody();

			if (resultados == null || resultados.isEmpty()) {
				return Optional.empty();
			}

			Map<String, Object> primero = resultados.get(0);
			double lat = Double.parseDouble(primero.get("lat").toString());
			double lon = Double.parseDouble(primero.get("lon").toString());
			String nombre = primero.get("display_name") != null ? primero.get("display_name").toString() : texto;

			return Optional.of(new Coordenadas(lat, lon, nombre));
		} catch (Exception e) {
			// Si falla la geocodificación no rompemos la petición, simplemente
			// no devolvemos coordenadas
			return Optional.empty();
		}
	}
}