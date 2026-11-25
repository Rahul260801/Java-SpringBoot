package com.example.project.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//update
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf->csrf.disable()).
                authorizeHttpRequests( auth->auth.
                requestMatchers(HttpMethod.GET,"/api/students/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/students/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

//                requestMatchers( "/api/students/**").permitAll()
//                .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.builder().username("user").password("{noop}password").roles("USER").build();
        UserDetails admin = User.builder().username("admin").password("{noop}admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user,admin);
    }

}
