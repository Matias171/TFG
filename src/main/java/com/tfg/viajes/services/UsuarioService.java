package com.tfg.viajes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.viajes.entities.Usuario;
import com.tfg.viajes.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	// RGISTRAR USUARIO
	public Usuario registrar(Usuario usuario) {
		
		// comprobamos si el email no esta en uso
		Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
		
		if (existente.isPresent()) {
			 throw new RuntimeException("Ya existe un usuario con ese email");
		}
		
		// si el emial es nuevo se guarda.
		return usuarioRepository.save(usuario);
	}
	
	public Optional<Usuario> buscarPorEmail(String email) {
	    return usuarioRepository.findByEmail(email);
	}
	
	// LOGIN comprobando el email y contraseña
	public Optional<Usuario> login(String email, String password) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		// comprobamos que el usuario existe y que la contraseña coincide
		if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
			return usuario;
		}
		
		// si no existe o la contraseña no coincide, devolvemos vacio
		return Optional.empty();
	}
	
	// OBTENER todos los usuarios
	public List<Usuario> obtenerTodos(){
		return usuarioRepository.findAll();
	}
	
	// OBTENER un usuario por su id
	public Optional<Usuario> obtenerPorId(Long id){
		return usuarioRepository.findById(id);
	}
}