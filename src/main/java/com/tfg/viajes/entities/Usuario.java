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
    private String fotoPerfil;  // Puede ser null si el usuario no sube foto
    // GETTERS Y SETTERS
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
    public String getEmail() {
        return email;
    }
}
