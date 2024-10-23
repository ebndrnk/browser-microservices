package org.ebndrnk.springbrauser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityBeans {


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/registration", "/login").anonymous()
                        .requestMatchers("/user-page").authenticated()
                        .anyRequest().permitAll())
        .formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .defaultSuccessUrl("http://localhost:8765/index", true)
                        .permitAll())
                .logout(lg -> lg.logoutSuccessUrl("http://localhost:8765/index")
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .build();
    }



}
