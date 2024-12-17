package com.consoleolog.anyaskapiserver.global.config;

import com.consoleolog.anyaskapiserver.global.security.filter.JwtCheckFilter;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomAccessDeniedHandler;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomAuthenticationFailHandler;
import com.consoleolog.anyaskapiserver.global.security.handler.CustomAuthenticationSuccessHandler;
import com.consoleolog.anyaskapiserver.v1.service.impl.CustomOAuth2UserServiceImpl;
import com.consoleolog.anyaskapiserver.v1.util.JwtProvider;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserServiceImpl oAuth2UserService;

    @Value("${client.address}")
    private String clientAddress;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request-> request.requestMatchers("/**").permitAll());

        http.cors(AbstractHttpConfigurer::disable);

        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtCheckFilter(), ExceptionTranslationFilter.class);

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

        http.exceptionHandling(exception->{
            exception.accessDeniedHandler(accessDeniedHandler());
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
    @Bean
    public JwtCheckFilter jwtCheckFilter(){
        return new JwtCheckFilter(jwtProvider());
    }

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
    * AccessDeniedHandler
    * */
    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }


}
