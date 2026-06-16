package com.tfg.viajes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.viajes.entities.Actividad;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
	// Actividades de un viaje, ordenadas por día y hora
	List<Actividad> findByViajeIdOrderByDiaAscHoraAsc(Long viajeId);
}