package com.emersondev.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(
          HttpSecurity http) throws Exception {

    http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth

                    // Público — login y registro
                    .requestMatchers("/api/auth/**").permitAll()

                    // Swagger — público para desarrollo
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html").permitAll()

                    // Solo ADMIN puede crear/editar/eliminar productos
                    .requestMatchers(HttpMethod.POST,
                            "/api/productos/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/productos/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/productos/**")
                    .hasRole("ADMIN")

                    // ADMIN y VENDOR pueden ver productos
                    .requestMatchers(HttpMethod.GET,
                            "/api/productos/**")
                    .hasAnyRole("ADMIN", "VENDOR")

                    // Solo ADMIN ve dashboard y liquidación
                    .requestMatchers("/api/dashboard/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/api/liquidacion/**")
                    .hasRole("ADMIN")

                    // ADMIN y VENDOR pueden registrar ventas
                    .requestMatchers("/api/ventas/**")
                    .hasAnyRole("ADMIN", "VENDOR")

                    // ADMIN, VENDOR pueden gestionar clientes
                    .requestMatchers("/api/clientes/**")
                    .hasAnyRole("ADMIN", "VENDOR")

                    // ADMIN puede registrar movimientos
                    .requestMatchers(HttpMethod.POST,
                            "/api/movimientos/**")
                    .hasRole("ADMIN")

                    // ADMIN, VENDOR pueden ver movimientos
                    .requestMatchers(HttpMethod.GET,
                            "/api/movimientos/**")
                    .hasAnyRole("ADMIN", "VENDOR")

                    // ADMIN puede gestionar almacenes (CRUD completo)
                    .requestMatchers(HttpMethod.POST,
                            "/api/almacenes/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/almacenes/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/almacenes/**")
                    .hasRole("ADMIN")

                    // ADMIN y VENDOR pueden ver almacenes (necesario para el select de ventas)
                    .requestMatchers(HttpMethod.GET,
                            "/api/almacenes/**")
                    .hasAnyRole("ADMIN", "VENDOR")

                    .requestMatchers("/api/gastos/**")
                    .hasRole("ADMIN")

                    .requestMatchers("/api/gamification/**")
                    .hasAnyRole("ADMIN", "VENDOR")

                    .requestMatchers("/api/reportes/**")
                    .hasRole("ADMIN")

                    // Cualquier otra ruta requiere autenticación
                    .anyRequest().authenticated())

            .sessionManagement(session -> session
                    .sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))

            .authenticationProvider(authenticationProvider())

            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Permite cualquier origen (frontend) temporalmente para desarrollo/producción
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider =
            new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
          AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
