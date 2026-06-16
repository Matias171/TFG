package com.tfg.viajes.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "itinerario")
public class Actividad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Día del viaje al que pertenece esta actividad
	@Column(nullable = false)
	private LocalDate dia;

	// Hora a la que ocurre (opcional)
	private LocalTime hora;

	@Column(nullable = false)
	private String titulo; // Ej: "Visita al Coliseo"

	@Column(length = 1000)
	private String descripcion; // Notas adicionales

	private String lugar; // Ej: "Coliseo, Roma" (texto libre)

	// Coordenadas del lugar (se rellenan automáticamente si es posible)
	private Double lat;
	private Double lon;

	@ManyToOne
	@JoinColumn(name = "viaje_id")
	@JsonIgnore // evita el bucle al serializar
	private Viaje viaje;

	// GETTERS Y SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDia() {
		return dia;
	}

	public void setDia(LocalDate dia) {
		this.dia = dia;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}
}