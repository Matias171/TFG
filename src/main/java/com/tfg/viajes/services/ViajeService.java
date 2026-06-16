package com.tfg.viajes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.viajes.entities.Viaje;
import com.tfg.viajes.entities.Participante;
import com.tfg.viajes.entities.Usuario;
import com.tfg.viajes.repository.ParticipanteRepository;
import com.tfg.viajes.repository.UsuarioRepository;
import com.tfg.viajes.repository.ViajeRepository;

@Service
public class ViajeService {
	
	@Autowired
	private ViajeRepository viajeRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ParticipanteRepository participanteRepository;
	
	// CREAR un viaje nuevo
	public Viaje crearViaje(Viaje viaje, Long creadorId) {
		
		Usuario creador = usuarioRepository.findById(creadorId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		// orElseThrow -> si esl Optional esta vacio lanza error
		
		viaje.setCreador(creador); // asiganmos el creador del viaje
		
		// guardamos el viaje en la base de datos
		Viaje viajeGuardado = viajeRepository.save(viaje);
		
		// El creador tambien es participante del viaje automaticamente
		Participante participante = new Participante();
		participante.setUsuario(creador);
		participante.setViaje(viajeGuardado);
		participanteRepository.save(participante);
		
		return viajeGuardado;
	}
	
	// OBTENER todos los viajes de un usuario
    public List<Viaje> obtenerViajesDeUsuario(Long usuarioId) {
        // Primero buscamos en qué viajes participa el usuario
        List<Participante> participaciones = participanteRepository.findByUsuarioId(usuarioId);

        return participaciones.stream()
                .map(p -> viajeRepository.findById(p.getViaje().getId()).orElse(null))
                .filter(v -> v != null)
                .distinct()
                .toList();
    }
    
    // OBTENER un viaje por su id
    public Optional<Viaje> obtenerPorId(Long id) {
    	
    	return viajeRepository.findById(id);
    }
    
    // AÑADIR un participante al viaje
    public Participante anyadirParticipante(Long viajeId, Long usuarioId) {
    	
    	Viaje viaje = viajeRepository.findById(viajeId).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
    	
    	Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    	
        // Comprobamos si el usuario ya es participante del viaje
        // Buscamos en la lista de participantes del viaje si ya existe ese usuario
        boolean yaExiste = participanteRepository.findByViajeId(viajeId)
            .stream()
            .anyMatch(p -> p.getUsuario().getId().equals(usuarioId));
        
        if (yaExiste) {
            throw new RuntimeException("Este usuario ya es participante del viaje");
        }
    	
    	// creamos la relacion entrel el usuario y el viaje
    	Participante participante = new Participante();
    	participante.setViaje(viaje);
    	participante.setUsuario(usuario);
    	
    	return participanteRepository.save(participante);
    }
    
    
	 // ELIMINAR un participante de un viaje
	 // No se puede eliminar al creador del viaje
	 public void eliminarParticipante(Long participanteId) {
	     
	     Participante participante = participanteRepository.findById(participanteId)
	         .orElseThrow(() -> new RuntimeException("Participante no encontrado"));
	     
	     Viaje viaje = participante.getViaje();
	     Usuario usuario = participante.getUsuario();
	     
	     // Comprobamos que no sea el creador del viaje
	     if (viaje.getCreador().getId().equals(usuario.getId())) {
	         throw new RuntimeException("No se puede eliminar al creador del viaje");
	     }
	     
	     participanteRepository.deleteById(participanteId);
	 }

}
