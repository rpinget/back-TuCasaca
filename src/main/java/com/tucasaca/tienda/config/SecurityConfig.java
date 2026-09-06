package com.tucasaca.tienda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tucasaca.tienda.repository.UsuarioRepository;
import com.tucasaca.tienda.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UsuarioRepository usuarioRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // login público
                        .requestMatchers("/api/auth/**").permitAll()
                        // registro público (queda en /api/usuarios/registro, no bajo /api/auth)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                        // cualquiera puede ver el catálogo de casacas
                        .requestMatchers(HttpMethod.GET, "/api/casacas/**").permitAll()
                        // solo usuarios autenticados pueden crear/editar/eliminar casacas
                        .requestMatchers(HttpMethod.POST, "/api/casacas/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/casacas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/casacas/**").authenticated()
                        // cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
