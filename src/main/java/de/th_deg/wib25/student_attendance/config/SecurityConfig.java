package de.th_deg.wib25.student_attendance.config;

import de.th_deg.wib25.student_attendance.service.UserService;
import org.springframework.context. annotation.Bean;
import org. springframework.context.annotation.Configuration;
import org.springframework.security. config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation. web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()  //  Alle Seiten benötigen Authentifizierung
                )
                .formLogin(form -> form
                        .permitAll()  //  Login-Seite für alle zugänglich
                )
                .logout(logout -> logout
                        .permitAll()  //  Logout für alle zugänglich
                )
                .csrf(csrf -> csrf.disable())  //  CSRF für Entwicklung deaktivieren
                .userDetailsService(userService);  //  UserService für Authentifizierung verwenden

        return http.build();
    }
}
