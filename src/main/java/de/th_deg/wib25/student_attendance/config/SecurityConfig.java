package de.th_deg.wib25.student_attendance.config;

import org.springframework.context. annotation.Bean;
import org. springframework.context.annotation.Configuration;
import org.springframework.security. config.annotation.web.builders. HttpSecurity;
import org.springframework.security.config.annotation. web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/courses/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                 // deine eigene Login-Seite
                        .defaultSuccessUrl("/courses", true) // <- immer nach Login auf Kursübersicht
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );
        return http.build();
    }
}
