package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long>{
	
	List<Viaje> findByCreadorId(Long creadorId);

}
