package com.tfg.viajes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.viajes.entities.Gasto;
import com.tfg.viajes.entities.Usuario;
import com.tfg.viajes.entities.Viaje;
import com.tfg.viajes.repository.GastoRepository;
import com.tfg.viajes.repository.UsuarioRepository;
import com.tfg.viajes.repository.ViajeRepository;

@Service
public class GastoService {

	@Autowired
	private GastoRepository gastoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ViajeRepository viajeRepository;
	
	// RREGISTRAR un gasto nuevo
	public Gasto registrarGasto(Gasto gasto, Long pagadorId, Long viajeId) {
		
		// verificamos si existen
		Usuario pagador = usuarioRepository.findById(pagadorId).orElseThrow(() -> new RuntimeException("Pagador no encontrado"));
		
		Viaje viaje = viajeRepository.findById(viajeId).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
	
		// asignamos el pagador y el viaje al gasto antes de guardarlo
		gasto.setPagador(pagador);
		gasto.setViaje(viaje);
		
		// guardamos el gasto en la base de datos y lo devolvemos
		return gastoRepository.save(gasto);
	}
	
	
	// OBTENER tofos los gastos de un viaje por id
	public List<Gasto> obtenerGastosDeViaje(Long viajeId) {
		return gastoRepository.findByViajeId(viajeId);
	}
	
	// OBTENER todos los gastos pagados por un usuario por id
	public List<Gasto> obtenerGastosDeUsuario(Long pagadorId) {
		return gastoRepository.findByPagadorId(pagadorId);
	}
	
	
	// ELIMINAR un gasto por id
	public void eliminarGasto(Long gastoId) {
		 
		// comprobamos primero que el gasto existe
		if (!gastoRepository.existsById(gastoId)) {
			throw new RuntimeException("Gasto no encontrado");
		}
		gastoRepository.deleteById(gastoId);
	}

}
