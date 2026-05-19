package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Participante;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long>{

	// Los participantes de un viaje en especifico
	List<Participante> findByViajeId(Long viajeId);
	
	// Los viajes en los que esta un usuario
	List<Participante> findByUsuarioId(Long usuarioId);
	
}
