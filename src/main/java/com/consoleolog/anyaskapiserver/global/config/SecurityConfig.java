package com.consoleolog.anyaskapiserver.global.config;

//import com.consoleolog.anyaskapiserver.global.security.filter.JwtCheckFilter;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomAccessDeniedHandler;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomAuthenticationFailHandler;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomAuthenticationSuccessHandler;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomLogoutSuccessHandler;
import com.consoleolog.anyaskapiserver.v1.service.impl.CustomOAuth2UserServiceImpl;
import com.consoleolog.anyaskapiserver.v1.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * @FileName		: SecurityConfig.java
 * @Author			: ACR
 * @Date			: 24. 12. 18.
 * @Description		: 최초 생성
 **/
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserServiceImpl oAuth2UserService;

    @Value("${client.address}")
    private String clientAddress;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.debug("=====================");
        log.debug("   SecurityConfig    ");
        log.debug("=====================");

        http.authorizeHttpRequests(request-> request.requestMatchers("/**").permitAll());

        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(cors->cors.configurationSource(corsConfigurationSource()));

        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler()));

//        http.addFilterBefore(jwtCheckFilter(), ExceptionTranslationFilter.class);

        http.oauth2Login(oauth2->{
            oauth2.loginPage("http://localhost:8501/");
            oauth2.userInfoEndpoint(config->config.userService(oAuth2UserService));
            oauth2.successHandler(authenticationSuccessHandler());
            oauth2.failureHandler(authenticationFailHandler());
        });

        http.formLogin(form->{
            form.loginPage("http://localhost:8501/");
            form.usernameParameter("userId");
            form.passwordParameter("userPwd");
            form.successHandler(authenticationSuccessHandler());
            form.failureHandler(authenticationFailHandler());
        });

        http.logout(logout->{
            logout.logoutUrl("/api/v1/logout");
            logout.logoutSuccessHandler(logoutSuccessHandler());
            logout.deleteCookies("access_token", "refresh_token");
        });


        return http.build();
    }

    /*
     * PasswordEncoder
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
    * JwtProvider
    * */
    @Bean
    public JwtProvider jwtProvider(){
        return new JwtProvider();
    }

    /*
    * JwtCheckFilter
    * */
//    @Bean
//    public JwtCheckFilter jwtCheckFilter(){
//        return new JwtCheckFilter(jwtProvider());
//    }

    /*
    * LoginSuccessHandler
    * */
    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler(jwtProvider());
    }

    /*
    * LoginFailHandler
    * */
    @Bean
    public CustomAuthenticationFailHandler authenticationFailHandler(){
        return new CustomAuthenticationFailHandler();
    }

    /*
    * LogoutSuccessHandler
    * */
    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    /*
    * AccessDeniedHandler
    * */
    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    /*
    * CorsConfig
    * */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
