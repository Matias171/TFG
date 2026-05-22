package com.tfg.viajes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.viajes.entities.Viaje;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
	List<Viaje> findByCreadorId(Long creadorId);
}