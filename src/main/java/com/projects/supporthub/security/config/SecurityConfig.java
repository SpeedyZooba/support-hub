package com.projects.supporthub.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired
    private UserDetailsService details;

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.
            authorizeHttpRequests((authz) -> authz
                .requestMatchers("/adminpanel/**").hasRole("ROLE_ADMIN").anyRequest().authenticated()
                .requestMatchers("/notices").hasAnyRole("ROLE_USER, ROLE_ADMIN").anyRequest().authenticated()
                .requestMatchers("/login").anonymous().anyRequest().authenticated()
                );
        http.formLogin((formLogin) -> formLogin
                .loginPage("/login.html")
                .permitAll()
                .defaultSuccessUrl("/home.html")
                .failureUrl("/login.html?error=true")
                )
            .logout((formLogout) -> formLogout
                .logoutUrl("logout.html")
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerConfig(HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(details).passwordEncoder(encoder());
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }
}