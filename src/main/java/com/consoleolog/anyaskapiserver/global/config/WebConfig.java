package com.consoleolog.anyaskapiserver.global.config;

import com.consoleolog.anyaskapiserver.global.common.LocalDateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Bean
    public LocalDateTimeFormatter timeFormatter(){
        return new LocalDateTimeFormatter();
    }

    @Bean
    public WebClient webClient(WebClient.Builder clientBuilder){
        return clientBuilder.build();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(timeFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type", "Cache-Control");
    }



}
