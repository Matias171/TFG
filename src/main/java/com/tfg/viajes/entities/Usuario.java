package com.tfg.viajes.entities;
import jakarta.persistence.*;
@Entity
@Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;
    @Column(nullable = false, unique = true)  // Email único y obligatorio
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nombre;
    
    
    // campos del perfil de usuario
    private String apodo;
    
    @Column(length = 500)
    private String descripcion;
    
    private String telefono;
    private String direccion;
    private String fechaNacimiento;   // formato "DD/MM/YYYY"
    
    // la foto se guarda en Base64, es un texto largo asi que usamos @Lob
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fotoPerfil;
    
    
    // GETTERS Y SETTERS
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFotoPerfil() {
		return fotoPerfil;
	}
	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
	public String getApodo() {
		return apodo;
	}
	public void setApodo(String apodo) {
		this.apodo = apodo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descriptcion) {
		this.descripcion = descriptcion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNcimiento) {
		this.fechaNacimiento = fechaNcimiento;
	}
	
	
    
}
