package com.tfg.viajes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.viajes.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}