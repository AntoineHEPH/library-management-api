package be.condorcet.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;

/**
 * Configuration Spring Security pour l'API Library Management.
 * 
 * Pour la phase de développement/test, tous les endpoints sont accessibles sans authentification.
 * À l'avenir, on peut ajouter des rôles USER/ADMIN et protéger les opérations sensibles.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure les règles d'accès HTTP.
     * Actuellement : Tous les endpoints /api/** sont publics (pas d'authentification requise)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                // Lecture publique
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/**").permitAll()
                // Écriture réservée aux ADMIN
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()) // activer HTTP Basic (API moderne)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Démo: utilisateurs en mémoire.
     * - user/user (ROLE_USER)
     * - admin/admin (ROLE_ADMIN)
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}user").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("{noop}admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
