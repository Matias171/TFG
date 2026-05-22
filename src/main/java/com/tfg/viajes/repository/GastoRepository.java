package com.tfg.viajes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.viajes.entities.Gasto;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
	// Los gastos de un viaje en especifico
	List<Gasto> findByViajeId(Long viajeId);
	
	// Los gastos pagados por un usuario en especifico
	List<Gasto> findByPagadorId(Long pagadorId);
}