package com.tfg.viajes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.viajes.entities.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
	// Los participantes de un viaje en especifico
	List<Participante> findByViajeId(Long viajeId);

	// Los viajes en los que esta un usuario
	List<Participante> findByUsuarioId(Long usuarioId);
}