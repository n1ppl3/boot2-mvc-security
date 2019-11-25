package ru.pipan.spring.boot2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static ru.pipan.spring.boot2.config.SecurityConstants.*;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails basic = User.withUsername(USER_NAME_WORKER).password("a")
                .passwordEncoder(encoder::encode)
                .roles(MY_ROLE_USER)
                .build();

        UserDetails manager = User.withUsername(USER_NAME_MANAGER).password("b")
                .passwordEncoder(encoder::encode)
                .roles(MY_ROLE_MANAGER)
                .build();

        return new InMemoryUserDetailsManager(basic, manager);
    }
}
