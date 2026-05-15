package com.emersondev.infrastructure.web.controller;

import com.emersondev.domain.exception.EmailYaRegistradoException;
import com.emersondev.domain.model.Usuario;
import com.emersondev.infrastructure.persistence.repository.UsuarioRepositoryAdapter;
import com.emersondev.infrastructure.web.dto.request.LoginRequest;
import com.emersondev.infrastructure.web.dto.request.RegisterRequest;
import com.emersondev.infrastructure.web.dto.response.AuthResponse;
import com.emersondev.security.JwtService;
import com.emersondev.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UsuarioRepositoryAdapter usuarioRepository;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;


  // POST: /api/auth/login
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
          @Valid @RequestBody LoginRequest request) {

    log.info("Intento de Login: {}", request.getEmail());

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));

    UserDetails userDetails = userDetailsService
            .loadUserByUsername(request.getEmail());

    Usuario usuario = usuarioRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la Base de datos"));

    String token = jwtService.generateToken(
            userDetails, usuario.getRol());

    log.info("Login exitoso: {}", request.getEmail());

    return ResponseEntity.ok(new AuthResponse(
            token,
            usuario.getId() != null ? usuario.getId().toString() : null,
            usuario.getEmail(),
            usuario.getNombre(),
            usuario.getRol(),
            86400000L));
  }

  // POST: /api/auth/register
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
          @Valid @RequestBody RegisterRequest request ) {

    log.info("Registrando usuario: {}", request.getEmail());

    if (usuarioRepository.existsByEmail(request.getEmail())) {
      throw new EmailYaRegistradoException(request.getEmail());
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(request.getNombre());
    usuario.setEmail(request.getEmail());
    usuario.setPassword(passwordEncoder.encode(
            request.getPassword()));
    usuario.setRol(request.getRol() != null ?
            request.getRol() : "VENDEDOR");
    usuario.setActivo(true);
    usuario.setCreatedAt(LocalDateTime.now());

    Usuario creado = usuarioRepository.save(usuario);

    UserDetails userDetails = userDetailsService
            .loadUserByUsername(creado.getEmail());

    String token = jwtService.generateToken(
            userDetails, creado.getRol());

    log.info("Usuario Registrado {}", creado.getEmail());

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new AuthResponse(
                    token,
                    creado.getId() != null ? creado.getId().toString() : null,
                    creado.getEmail(),
                    creado.getNombre(),
                    creado.getRol(),
                    86400000L));
  }

  // GET: /api/auth/users
  @GetMapping("/users")
  public ResponseEntity<List<Map<String, String>>> getLoginUsers() {
    // Pedimos todos los usuarios a la base de datos
    List<Usuario> usuarios = usuarioRepository.findAll();

    // Mapeamos solo la información segura (sin el password/PIN)
    List<Map<String, String>> response = usuarios.stream()
            .filter(Usuario::isActivo) // Solo mostramos los activos
            .map(u -> Map.of(
                    "id", u.getId() != null ? u.getId().toString() : "",
                    "nombre", u.getNombre(),
                    "email", u.getEmail(),
                    "rol", u.getRol()
            ))
            .collect(Collectors.toList());

    return ResponseEntity.ok(response);
  }

  // PUT: /api/auth/users/{id}
  @PutMapping("/users/{id}")
  public ResponseEntity<Map<String, String>> updateUser(
          @PathVariable UUID id,
          @RequestBody Map<String, String> request) {
      
      Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
      
      if (request.containsKey("nombre")) usuario.setNombre(request.get("nombre"));
      if (request.containsKey("email")) {
        // Simple validación de que si el email cambia, no esté en uso.
        if (!usuario.getEmail().equals(request.get("email")) && usuarioRepository.existsByEmail(request.get("email"))) {
            throw new EmailYaRegistradoException(request.get("email"));
        }
        usuario.setEmail(request.get("email"));
      }
      if (request.containsKey("rol")) usuario.setRol(request.get("rol"));
      if (request.containsKey("password") && !request.get("password").isEmpty()) {
          usuario.setPassword(passwordEncoder.encode(request.get("password")));
      }

      usuarioRepository.save(usuario);
      return ResponseEntity.ok(Map.of("message", "Usuario actualizado correctamente"));
  }

  // DELETE: /api/auth/users/{id}
  @DeleteMapping("/users/{id}")
  public ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID id) {
      Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
      usuario.setActivo(false); // Soft Delete para no romper Foreign Keys
      usuarioRepository.save(usuario);
      return ResponseEntity.ok(Map.of("message", "Usuario inhabilitado con éxito"));
  }
}
