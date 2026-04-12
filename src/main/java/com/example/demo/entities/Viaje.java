package com.example.demo.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "viajes")

public class Viaje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nombre;
	
	private String descripcion;
	private String destino;
	
	// localDate -> guarda solo la fecha (sin la hora), año - mes - dia
	private LocalDate fechaInciio;
	private LocalDate fechaFin;
	
	// ManyToOne -> muchos viajes pueden tener el mismo usuario
	// JoinColumn -> en la tabla "viajes" se guarda una columna "creador_id" que es quien creo el viaje
	@ManyToOne
	@JoinColumn(name = "creador_id")
	private Usuario creador;
	
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public LocalDate getFechaInciio() {
		return fechaInciio;
	}

	public void setFechaInciio(LocalDate fechaInciio) {
		this.fechaInciio = fechaInciio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Usuario getCreador() {
		return creador;
	}

	public void setCreador(Usuario creador) {
		this.creador = creador;
	}
	
}
