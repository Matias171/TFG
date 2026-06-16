package com.tfg.viajes.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tfg.viajes.entities.Participante;
import com.tfg.viajes.entities.Usuario;
import com.tfg.viajes.repository.ParticipanteRepository;
import com.tfg.viajes.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ParticipanteRepository participanteRepository;

    // GET /usuarios → obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }
    
 // GET /api/usuarios/buscar?email=correo@ejemplo.com
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorEmail(@RequestParam String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // GET /usuarios/{id} → obtener un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // POST /usuarios/registro → registrar usuario nuevo
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /usuarios/login → iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> resultado = usuarioService.login(
            usuario.getEmail(), usuario.getPassword()
        );
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.status(401).body("Email o contraseña incorrectos");
    }
    
 // GET /api/usuarios/perfil/{id} → devuelve el perfil completo del usuario
    @GetMapping("/perfil/{id}")
    public ResponseEntity<?> getPerfil(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/usuarios/perfil/{id} → actualiza el perfil
    @PutMapping("/perfil/{id}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long id, @RequestBody Usuario datosNuevos) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Actualizamos solo los campos del perfil, no el email ni la contraseña
            if (datosNuevos.getNombre() != null)          usuario.setNombre(datosNuevos.getNombre());
            if (datosNuevos.getApodo() != null)           usuario.setApodo(datosNuevos.getApodo());
            if (datosNuevos.getDescripcion() != null)     usuario.setDescripcion(datosNuevos.getDescripcion());
            if (datosNuevos.getTelefono() != null)        usuario.setTelefono(datosNuevos.getTelefono());
            if (datosNuevos.getDireccion() != null)       usuario.setDireccion(datosNuevos.getDireccion());
            if (datosNuevos.getFechaNacimiento() != null) usuario.setFechaNacimiento(datosNuevos.getFechaNacimiento());
            if (datosNuevos.getFotoPerfil() != null)      usuario.setFotoPerfil(datosNuevos.getFotoPerfil());

            Usuario actualizado = usuarioService.guardar(usuario);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/usuarios/historial/{id} → viajes en los que ha participado
    @GetMapping("/historial/{id}")
    public ResponseEntity<?> getHistorial(@PathVariable Long id) {
        List<Participante> participaciones = participanteRepository.findByUsuarioId(id);
        // Devolvemos solo id, nombre y fechas de cada viaje
        List<Map<String, Object>> historial = participaciones.stream()
            .map(p -> {
                Map<String, Object> item = new java.util.LinkedHashMap<>();
                item.put("id", p.getViaje().getId());
                item.put("nombre", p.getViaje().getNombre());
                item.put("destino", p.getViaje().getDestino());
                item.put("fechaInicio", p.getViaje().getFechaInicio());
                return item;
            })
            .distinct()
            .toList();
        return ResponseEntity.ok(historial);
    }
}