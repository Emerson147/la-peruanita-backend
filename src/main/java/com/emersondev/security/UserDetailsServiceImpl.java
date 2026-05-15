package com.emersondev.security;

import com.emersondev.infrastructure.persistence.repository.UsuarioRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UsuarioRepositoryAdapter usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String email)
          throws UsernameNotFoundException {

    return usuarioRepository.findByEmail(email)
            .map(usuario -> {
              log.info("Usuario encontrado: {}", email);
              return new User(
                      usuario.getEmail(),
                      usuario.getPassword(),
                      List.of(new SimpleGrantedAuthority(
                              "ROLE_" + usuario.getRol())));
            })
            .orElseThrow(() -> {
              log.error("Usuario no encontrado: {}", email);
              return new UsernameNotFoundException(
                      "Usuario no encontrado: " + email);
            });
  }
}
