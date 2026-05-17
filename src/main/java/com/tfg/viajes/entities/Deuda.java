package com.tfg.viajes.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "deudas")
public class Deuda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "deudor_id")
	private Usuario deudor; // Quien debe el dinero
	@ManyToOne
	@JoinColumn(name = "acreedor_id")
	private Usuario acreedor; // A quien le deben el dinero
	private Double cantidad; // Cuánto se debe
	// false = pendiente de pago, true = deuda ya saldada
	private Boolean pagada = false;
	@ManyToOne
	@JoinColumn(name = "viaje_id")
	private Viaje viaje; // A qué viaje pertenece esta deuda
// GETTERS

	public Long getId() {
		return id;
	}

	public Usuario getDeudor() {
		return deudor;
	}

	public Usuario getAcreedor() {
		return acreedor;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public Boolean getPagada() {
		return pagada;
	}

	public Viaje getViaje() {
		return viaje;
	}

// SETTERS

	public void setId(Long id) {
		this.id = id;
	}

	public void setDeudor(Usuario deudor) {
		this.deudor = deudor;
	}

	public void setAcreedor(Usuario acreedor) {
		this.acreedor = acreedor;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public void setPagada(Boolean pagada) {
		this.pagada = pagada;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}
}
