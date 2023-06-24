package com.projects.supporthub.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.projects.supporthub.security.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired
    private UserDetailsService details;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.
            authorizeHttpRequests((authz) -> authz
                .requestMatchers("/styles/**").permitAll()
                .requestMatchers("/resources/**").permitAll()
                .requestMatchers("/home").hasAnyRole("USER, ADMIN")
                .requestMatchers("/notices").hasAnyRole("USER, ADMIN")
                .requestMatchers("/adminpanel/**").hasRole("ADMIN")
                .requestMatchers("/login*").anonymous().anyRequest().authenticated()
                )
            .formLogin((formLogin) -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler())
                .failureUrl("/login?error")
                .permitAll()
                )
            .rememberMe((rember) -> rember
                .disable()
                )
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionConcurrency((concurrency) -> concurrency
                    .maximumSessions(1)
                    .expiredUrl("/login?expired")
                    .maxSessionsPreventsLogin(true)
                    )
                )
            .logout((formLogout) -> formLogout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
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

    @Bean
    public LoginSuccessHandler successHandler()
    {
        return new LoginSuccessHandler();
    }
}