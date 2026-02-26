package pe.gob.munipaucarpata.archivocentral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuración global de CORS para permitir comunicación con el frontend
 * Angular.
 *
 * Orígenes permitidos:
 * - http://localhost:4200 (Angular dev server)
 * - http://localhost:4300 (Angular alternativo)
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        var config = new CorsConfiguration();

        // Orígenes permitidos (Angular dev server)
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:4300"));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos
        config.setAllowedHeaders(List.of("*"));

        // Permitir cookies/credenciales
        config.setAllowCredentials(true);

        // Tiempo de cache para preflight
        config.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
