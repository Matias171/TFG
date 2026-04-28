package com.tfg.viajes.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "gasto")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private Double importe;

    @ManyToOne
    @JoinColumn(name = "pagador_id")
    private Usuario pagador;

    @ManyToOne
    @JoinColumn(name = "viaje_id")
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

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
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