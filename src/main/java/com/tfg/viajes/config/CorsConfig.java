package com.tfg.viajes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
		        .allowedOrigins(
		                "http://localhost:4200",           // desarrollo local
		                "https://viajesapp.vercel.app",    // producción Vercel (cambia esto después)
		                "https://*.vercel.app"             // cualquier preview de Vercel
		            )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}