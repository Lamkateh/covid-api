package org.polytech.covidapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http.authorizeHttpRequests((authz) -> authz
                // ajouter les règles de la plus spécifique à la plus générale
                .antMatchers("/private/**").authenticated()
                .antMatchers("/public/**").permitAll()
                .mvcMatchers("/private/admin").hasAnyRole("ADMIN" , "SUPER_ADMIN")
                .mvcMatchers("/private/superAdmin").hasRole("SUPER_ADMIN")
                .mvcMatchers("/private/user").hasRole("USER")
                .mvcMatchers("/private/doctor").hasRole("DOCTOR")

        )
                .httpBasic(withDefaults())
                .cors().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);// On rend les session stateless

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
