package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "deudas")


public class Deuda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "deudor_id")
	private Usuario deudor;
	
	@ManyToOne
	@JoinColumn(name = "acreedor_id")
	private Usuario acreedor;
	
	private Double cantidad;
	
	// false es deuda pendiente, true es deuda pagada
	private Boolean pagada = false;
	
	@ManyToOne
	@JoinColumn(name = "viaje_id")
	private Viaje viaje;
	
	

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
