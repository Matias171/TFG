package com.tfg.viajes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.viajes.entities.Deuda;

public interface DeudaRepository extends JpaRepository<Deuda, Long> {
	// Las deudas de un viaje
	List<Deuda> findByViajeId(Long viajeId);

	// Lo que debe un usuario em especifico en todos sus viajes
	List<Deuda> findByDeudorId(Long deudorId);

	// Lo que le deben a un usuario en especifico
	List<Deuda> findByAcreedorId(Long acreedorId);
}