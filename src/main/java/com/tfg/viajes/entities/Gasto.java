package com.tfg.viajes.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "gasto")
public class Gasto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String descripcion; // Ej: "Cena en restaurante"
	@Column(nullable = false)
	private Double cantidad; // Ej: 60.0 (euros)
	private LocalDate fecha; // Fecha en la que se realizó el gasto
	// Quién pagó este gasto
	@ManyToOne
	@JoinColumn(name = "pagador_id")
	private Usuario pagador;
	// A qué viaje pertenece este gasto
	@ManyToOne
	@JoinColumn(name = "viaje_id")
	@JsonIgnore  // evita el bucle al serializar gastos
	private Viaje viaje;

	// getters y setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Usuario getPagador() {
		return pagador;
	}

	public void setPagador(Usuario pagador) {
		this.pagador = pagador;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

}
