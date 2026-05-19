package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Deuda;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long>{
	
	// Las deudas de un viaje
	List<Deuda> findByViajeId(Long viajeId);
	
	// Lo que debe un usuario em especifico en todos sus viajes
	List<Deuda> findByDeudorId(Long deudorId);
	
	// Lo que le deben a un usuario en especifico
	List<Deuda> findByAcreedorId(Long acreedorId);

}
