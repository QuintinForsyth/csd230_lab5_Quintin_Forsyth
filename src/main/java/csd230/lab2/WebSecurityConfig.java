package csd230.lab2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // annotation to identify this as a configuration file
@EnableWebSecurity // annotation to allow this application to use spring security
public class WebSecurityConfig {

    @Bean // identifies this as a spring bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll() // Permits everyone to access /home
                        // Testing routes for lab4
                        .requestMatchers("/", "/rest/book").permitAll() // Permits everyone to access /book
                        .requestMatchers("/", "/rest/magazine").permitAll() // Permits everyone to access /magazine
                        .requestMatchers("/", "/rest/discmag").permitAll() // Permits everyone to access /discmag
                        .requestMatchers("/", "/rest/ticket").permitAll() // Permits everyone to access /ticket
                        .requestMatchers("/", "/rest/cart").permitAll() // Permits everyone to access /ticket
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login") // identifies the login page
                        .permitAll() // permits all to access the login page
                )
                .logout((logout) -> logout.permitAll()); // Allows users to access the logout

        return http.build(); // creates and returns the security filter chain
    }

    @Bean // identifies this as a spring bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder() // Uses a basic password encoder
                        .username("user") // Sets the username to user
                        .password("password") // Sets the password to password
                        .roles("USER") // Assigns the USER role
                        .build(); // Builds object userDetails

        return new InMemoryUserDetailsManager(user); // Stores the user in memory
    }
}
