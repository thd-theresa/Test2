
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/courses/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // eigene Login-Seite (falls du eine hast); wenn nicht, diese Zeile weglassen
                        .loginPage("/login")
                        // nach erfolgreichem Login IMMER zu /courses
                        .defaultSuccessUrl("/courses", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }
}
