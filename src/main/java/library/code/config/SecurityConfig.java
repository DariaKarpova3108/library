package library.code.config;

import library.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;


/*.requestMatchers("/api/users/**").hasAnyRole("ADMIN", "READER")
                   .requestMatchers(HttpMethod.GET, "/api/publishers/**").hasAnyRole("ADMIN",
                           "READER")
                   .requestMatchers("/api/readers/**").hasRole("ADMIN")
                   .requestMatchers("/api/publishers/**").hasRole("ADMIN")
                   .requestMatchers(HttpMethod.GET, "/api/libraryCards/**").hasAnyRole("ADMIN",
                           "READER")
                   .requestMatchers(HttpMethod.GET, "/api/authors/**").hasAnyRole("ADMIN",
                           "READER")
                   .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("ADMIN", "READER")
                   .requestMatchers(HttpMethod.GET, "/api/genres/**").hasAnyRole("ADMIN",
                           "READER")
                   .requestMatchers("/api/administrations/**").hasRole("ADMIN")
                   .requestMatchers(HttpMethod.GET, "/api/libraryCardBooks/**")
                   .hasAnyRole("ADMIN", "READER")
                   .requestMatchers(HttpMethod.PUT, "/api/libraryCardBooks/**")
                   .hasAnyRole("ADMIN", "READER")*/

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .build();
//    }

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/login/**", "/welcome").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter())))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public JwtAuthenticationConverter authenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            String role = jwt.getClaimAsString("role");
            if (role != null) {
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        });
        return converter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder auth = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(daoAuthProvider(auth)); // добавляем ваш AuthenticationProvider
        return auth.build();
    }


    @Bean
    public AuthenticationProvider daoAuthProvider(AuthenticationManagerBuilder auth) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
