package com.tfg.viajes.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tfg.viajes.entities.Viaje;

/**
 * Servicio que obtiene el clima del destino de un viaje usando la API
 * gratuita Open-Meteo (no requiere clave de API).
 */
@Service
public class ClimaService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GeoService geoService;

	private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast";
	private static final String ARCHIVE_URL = "https://archive-api.open-meteo.com/v1/archive";

	// Devuelve un mapa con: destino, lat, lon, actual (clima de ahora) y
	// pronostico (lista diaria) para las fechas del viaje
	@SuppressWarnings("unchecked")
	public Map<String, Object> obtenerClimaDeViaje(Viaje viaje) {
		Map<String, Object> resultado = new LinkedHashMap<>();

		String destino = viaje.getDestino();
		Optional<GeoService.Coordenadas> coords = geoService.geocodificar(destino);

		if (coords.isEmpty()) {
			resultado.put("disponible", false);
			resultado.put("mensaje", "No se ha podido localizar el destino \"" + destino + "\" en el mapa.");
			return resultado;
		}

		GeoService.Coordenadas c = coords.get();
		resultado.put("disponible", true);
		resultado.put("destino", destino);
		resultado.put("lugarEncontrado", c.nombre);
		resultado.put("lat", c.lat);
		resultado.put("lon", c.lon);

		LocalDate hoy = LocalDate.now();
		LocalDate inicio = viaje.getFechaInicio() != null ? viaje.getFechaInicio() : hoy;
		LocalDate fin = viaje.getFechaFin() != null ? viaje.getFechaFin() : hoy.plusDays(6);

		try {
			boolean viajePasado = fin.isBefore(hoy);

			if (viajePasado) {
				// Viaje ya terminado -> usamos datos históricos
				LocalDate finArchivo = fin.isAfter(hoy.minusDays(6)) ? hoy.minusDays(6) : fin;
				if (finArchivo.isBefore(inicio)) finArchivo = inicio;

				Map<String, Object> datos = llamarOpenMeteo(ARCHIVE_URL, c.lat, c.lon, inicio, finArchivo, false);
				resultado.put("modo", "historico");
				resultado.put("pronostico", extraerPronosticoDiario(datos));
				resultado.put("actual", null);
			} else {
				// Viaje en curso o futuro -> usamos la previsión
				LocalDate inicioForecast = inicio.isBefore(hoy) ? hoy : inicio;
				LocalDate maxForecast = hoy.plusDays(15);
				LocalDate finForecast = fin.isAfter(maxForecast) ? maxForecast : fin;
				if (finForecast.isBefore(inicioForecast)) finForecast = inicioForecast;

				Map<String, Object> datos = llamarOpenMeteo(FORECAST_URL, c.lat, c.lon, inicioForecast, finForecast, true);
				resultado.put("modo", "prevision");
				resultado.put("pronostico", extraerPronosticoDiario(datos));
				resultado.put("actual", extraerClimaActual(datos));

				if (fin.isAfter(maxForecast)) {
					resultado.put("aviso",
							"La previsión solo está disponible hasta " + maxForecast
									+ ". Más cerca de la fecha del viaje podrás ver el resto de días.");
				}
			}
		} catch (Exception e) {
			resultado.put("disponible", false);
			resultado.put("mensaje", "No se ha podido obtener el clima en este momento.");
		}

		return resultado;
	}

	private Map<String, Object> llamarOpenMeteo(String baseUrl, double lat, double lon,
			LocalDate inicio, LocalDate fin, boolean incluirActual) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("latitude", lat)
				.queryParam("longitude", lon)
				.queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum,weathercode")
				.queryParam("timezone", "auto")
				.queryParam("start_date", inicio.toString())
				.queryParam("end_date", fin.toString());

		if (incluirActual) {
			builder.queryParam("current_weather", "true");
		}

		return restTemplate.getForObject(builder.toUriString(), Map.class);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> extraerClimaActual(Map<String, Object> datos) {
		Map<String, Object> actual = (Map<String, Object>) datos.get("current_weather");
		if (actual == null) return null;

		Map<String, Object> resultado = new LinkedHashMap<>();
		resultado.put("temperatura", actual.get("temperature"));
		resultado.put("vientoKmh", actual.get("windspeed"));
		int codigo = ((Number) actual.get("weathercode")).intValue();
		resultado.put("codigo", codigo);
		resultado.put("descripcion", descripcionTiempo(codigo));
		resultado.put("icono", iconoTiempo(codigo));
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> extraerPronosticoDiario(Map<String, Object> datos) {
		List<Map<String, Object>> pronostico = new ArrayList<>();

		Map<String, Object> daily = (Map<String, Object>) datos.get("daily");
		if (daily == null) return pronostico;

		List<String> fechas = (List<String>) daily.get("time");
		List<Number> max = (List<Number>) daily.get("temperature_2m_max");
		List<Number> min = (List<Number>) daily.get("temperature_2m_min");
		List<Number> precipitacion = (List<Number>) daily.get("precipitation_sum");
		List<Number> codigos = (List<Number>) daily.get("weathercode");

		if (fechas == null) return pronostico;

		for (int i = 0; i < fechas.size(); i++) {
			Map<String, Object> dia = new LinkedHashMap<>();
			dia.put("fecha", fechas.get(i));
			dia.put("tempMax", max != null ? max.get(i) : null);
			dia.put("tempMin", min != null ? min.get(i) : null);
			dia.put("precipitacionMm", precipitacion != null ? precipitacion.get(i) : null);
			int codigo = codigos != null ? codigos.get(i).intValue() : 0;
			dia.put("codigo", codigo);
			dia.put("descripcion", descripcionTiempo(codigo));
			dia.put("icono", iconoTiempo(codigo));
			pronostico.add(dia);
		}

		return pronostico;
	}

	// Traduce el código WMO de Open-Meteo a una descripción en español
	private String descripcionTiempo(int codigo) {
		if (codigo == 0) return "Despejado";
		if (codigo == 1 || codigo == 2) return "Parcialmente nublado";
		if (codigo == 3) return "Nublado";
		if (codigo == 45 || codigo == 48) return "Niebla";
		if (codigo >= 51 && codigo <= 57) return "Llovizna";
		if (codigo >= 61 && codigo <= 67) return "Lluvia";
		if (codigo >= 71 && codigo <= 77) return "Nieve";
		if (codigo >= 80 && codigo <= 82) return "Chubascos";
		if (codigo >= 85 && codigo <= 86) return "Chubascos de nieve";
		if (codigo >= 95) return "Tormenta";
		return "Sin datos";
	}

	// Devuelve un emoji representativo del tiempo
	private String iconoTiempo(int codigo) {
		if (codigo == 0) return "☀️";
		if (codigo == 1 || codigo == 2) return "⛅";
		if (codigo == 3) return "☁️";
		if (codigo == 45 || codigo == 48) return "🌫️";
		if (codigo >= 51 && codigo <= 57) return "🌦️";
		if (codigo >= 61 && codigo <= 67) return "🌧️";
		if (codigo >= 71 && codigo <= 77) return "❄️";
		if (codigo >= 80 && codigo <= 82) return "🌧️";
		if (codigo >= 85 && codigo <= 86) return "🌨️";
		if (codigo >= 95) return "⛈️";
		return "🌡️";
	}
}