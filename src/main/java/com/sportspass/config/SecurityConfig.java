package com.sportspass.config;

import com.sportspass.jwt.JwtRequestFilter;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Collections;
import springfox.documentation.spi.service.contexts.SecurityContext;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    JwtRequestFilter jwtRequestFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                                .requestMatchers("/authenticate").permitAll()
                                .requestMatchers("/api/user/login").permitAll()
                                .requestMatchers("/api/user/register").permitAll()
                                .requestMatchers("/api/account-user/**").permitAll()
                                .requestMatchers("/api/packages/**").permitAll()
                                .requestMatchers("/api/credit-card/create{userId}").permitAll()
                                //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                                //  .requestMatchers("/api/user/**").hasRole("USER")
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                        "/swagger-resources/**", "/swagger-ui.html",
                                        "/webjars/**").permitAll()
                               // .requestMatchers("/api/v3/random").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                ).csrf().disable()
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
                 return http.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sportspass.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }
    private ApiKey apiKey() {
        return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, In.HEADER.toValue());
    }
    private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
        return SecurityContext.builder ()
                .securityReferences(defaultAuth())
                .build();
    }
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("JWT", authorizationScopes));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}