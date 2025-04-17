package io.github.caioosm.libraryapi.config;

import io.github.caioosm.libraryapi.security.CustomAuthentication;
import io.github.caioosm.libraryapi.security.JwtCustomAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import io.github.caioosm.libraryapi.security.LoginSocialSuccessHandler;

import java.util.Collection;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration { // resource server configuration

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSocialSuccessHandler successHandler,
            JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer -> {
                    configurer.loginPage("/login");
                })
                /*
                http basic caso seja necessario o cadastro do client, ja que n tem outro jeito por enquanto kk
                 */
//                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
//                    authorize.requestMatchers(HttpMethod.POST,"/autores/**").hasRole("ADMIN");
//                    authorize.requestMatchers(HttpMethod.GET,"/autores").hasAnyRole("ADMIN", "USER");
//                    authorize.requestMatchers("/livros/**").hasAnyRole("ADMIN", "USER");

                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    oauth2
                        .loginPage("/login")
                        .successHandler(successHandler);
                })
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(
                    "/v2/api-docs/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**"
            );
    }

    //configura o prefix ROLE
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    //configura no token jwt o prefix SCOPE
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenComInformacoesCustomizadas(){
        return context -> {
            var principal = context.getPrincipal();

            if (principal instanceof CustomAuthentication authentication){
                OAuth2TokenType tipoToken = context.getTokenType();

                if (OAuth2TokenType.ACCESS_TOKEN.equals(tipoToken)){
                    Collection<GrantedAuthority> authorities = authentication.getAuthorities();
                    List<String> authoritiesList = authorities.stream().map(GrantedAuthority::getAuthority).toList();

                    context
                            .getClaims()
                            .claim("authorities", authoritiesList)
                            .claim("email", authentication.getUsuario().getEmail());
                }
            }
        };
    }

}
