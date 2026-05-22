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

	// GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getDeudor() {
		return deudor;
	}

	public void setDeudor(Usuario deudor) {
		this.deudor = deudor;
	}

	public Usuario getAcreedor() {
		return acreedor;
	}

	public void setAcreedor(Usuario acreedor) {
		this.acreedor = acreedor;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Boolean getPagada() {
		return pagada;
	}

	public void setPagada(Boolean pagada) {
		this.pagada = pagada;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

}
