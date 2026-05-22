package com.tfg.viajes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.viajes.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	// Puede que haya un resultado o no
	Optional<Usuario> findByEmail(String email); // evitar emails duplicados

	}