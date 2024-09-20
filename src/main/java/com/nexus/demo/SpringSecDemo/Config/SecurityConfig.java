package com.nexus.demo.SpringSecDemo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth // Filtro de las páginas que se puede acceder sin iniciar sesión y donde es requerido
                    .requestMatchers("/", "/login", "/register", "logout").permitAll()
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login") // Especifica la página de login
                    .successHandler(authSuccessHandler()) // Manejador de éxito de autenticación
                    .permitAll()
            )
            .logout(config -> config.logoutSuccessUrl("/")) // Si el usuario cierra sesión lo redirije automaticamente a la página de inicio
                .exceptionHandling(exceptions -> exceptions.accessDeniedPage("/403")); // Configurando página de error

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return (request, response, authentication) -> {
            var roles = authentication.getAuthorities();
            if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/dashboard");
            } else if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
                response.sendRedirect("/user/dashboard");
            } else {
                response.sendRedirect("/");
            }
        };
    }
}
