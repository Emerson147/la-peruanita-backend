//package com.emersondev.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/api/**")
//            .allowedOrigins(
//                    "http://localhost:4200",  // Angular desarrollo
//                    "http://localhost:3000",   // otro cliente si lo necesitas
//                    "http://161.35.96.206:8080", // produccion
//                    "http://tiendas-denraf.vercel.app", // vercel produccion
//                    "https://tiendas-denraf.vercel.app" // vercel produccion
//
//
//            )
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .allowedHeaders("*")
//            .allowCredentials(true)
//            .maxAge(3600);
//  }
//}
