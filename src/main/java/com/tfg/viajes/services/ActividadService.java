package com.tfg.viajes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.viajes.entities.Actividad;
import com.tfg.viajes.entities.Viaje;
import com.tfg.viajes.repository.ActividadRepository;
import com.tfg.viajes.repository.ViajeRepository;

@Service
public class ActividadService {

	@Autowired
	private ActividadRepository actividadRepository;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private GeoService geoService;

	// Listar las actividades del itinerario de un viaje (ordenadas por día y hora)
	public List<Actividad> obtenerItinerarioDeViaje(Long viajeId) {
		return actividadRepository.findByViajeIdOrderByDiaAscHoraAsc(viajeId);
	}

	// Crear una actividad nueva dentro del itinerario de un viaje
	public Actividad crearActividad(Actividad actividad, Long viajeId) {
		Viaje viaje = viajeRepository.findById(viajeId)
				.orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

		actividad.setViaje(viaje);
		geocodificarSiHaceFalta(actividad, viaje);

		return actividadRepository.save(actividad);
	}

	// Actualizar una actividad existente
	public Actividad actualizarActividad(Long id, Actividad datos) {
		Actividad actividad = actividadRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

		boolean lugarCambiado = datos.getLugar() != null && !datos.getLugar().equals(actividad.getLugar());

		if (datos.getDia() != null) actividad.setDia(datos.getDia());
		actividad.setHora(datos.getHora());
		if (datos.getTitulo() != null) actividad.setTitulo(datos.getTitulo());
		actividad.setDescripcion(datos.getDescripcion());
		actividad.setLugar(datos.getLugar());

		
		return actividadRepository.save(actividad);
	}

	// Eliminar una actividad del itinerario
	public void eliminarActividad(Long id) {
		if (!actividadRepository.existsById(id)) {
			throw new RuntimeException("Actividad no encontrada");
		}
		actividadRepository.deleteById(id);
	}

	// Intenta rellenar lat/lon de la actividad a partir del campo "lugar"
	private void geocodificarSiHaceFalta(Actividad actividad, Viaje viaje) {
		if (actividad.getLat() != null && actividad.getLon() != null) return;
		if (actividad.getLugar() == null || actividad.getLugar().isBlank()) return;

		// Combinamos el lugar con el destino del viaje para que la búsqueda
		// sea más precisa (ej: "Coliseo, Roma")
		String texto = actividad.getLugar();
		if (viaje != null && viaje.getDestino() != null && !viaje.getDestino().isBlank()) {
			texto = texto + ", " + viaje.getDestino();
		}

		Optional<GeoService.Coordenadas> coords = geoService.geocodificar(texto);
		coords.ifPresent(c -> {
			actividad.setLat(c.lat);
			actividad.setLon(c.lon);
		});
	}
}