package de.th_deg.wib25.student_attendance.config;

import org.springframework.context. annotation.Bean;
import org. springframework.context.annotation.Configuration;
import org.springframework.security. config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation. web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        . anyRequest().permitAll()  //  Alle Seiten erlauben
                )
                .csrf(csrf -> csrf.disable());  //  CSRF für Entwicklung deaktivieren

        return http.build();
    }
}
