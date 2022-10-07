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
                .mvcMatchers("/public").permitAll()
                .mvcMatchers("/private").authenticated()
                .mvcMatchers("/private/admin").hasAuthority("ADMIN")
                .mvcMatchers("/private/superAdmin").hasAuthority("SUPER_ADMIN")
                .mvcMatchers("/private/user").hasAuthority("USER")
                .mvcMatchers("/private/doctor").hasAuthority("DOCTOR"))
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
