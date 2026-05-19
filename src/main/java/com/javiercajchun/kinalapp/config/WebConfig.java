package com.javiercajchun.kinalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register").permitAll()

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/clientes", "/clientes/estado", "/clientes/buscar", "/clientes/nuevo").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/clientes/guardar").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/clientes/editar/**", "/clientes/eliminar/**").hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/productos", "/productos/activos", "/productos/nuevo").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/productos/guardar").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/productos/editar/**", "/productos/eliminar/**").hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/ventas", "/ventas/detalle/**", "/ventas/nueva").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/ventas/guardar").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ventas/eliminar/**").hasRole("ADMIN")

                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/detalleVentas", "/detalleVentas/venta/**", "/detalleVentas/nuevo/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/detalleVentas/guardar").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/dashboard", "/").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer + "?error=permisos");
            } else {
                response.sendRedirect("/acceso-denegado");
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user@gmail.com")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin@gmail.com")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}