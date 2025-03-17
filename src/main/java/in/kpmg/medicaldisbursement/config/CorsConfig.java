package in.kpmg.medicaldisbursement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        , http://apmsidc.staging.ap.gov.in
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://103.129.72.100", "http://localhost:3000", "http://141.148.194.18:8080")); // Add your local dev origin
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://apmsidc.staging.ap.gov.in")); // Add your local dev origin
//        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Add your local dev origin
//        corsConfiguration.addAllowedOrigin("http://141.148.194.18:8080"); // Add other required origins
        corsConfiguration.addAllowedHeader("*"); // Allow all headers
        corsConfiguration.addAllowedMethod("*"); // Allow all methods
        corsConfiguration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply CORS config for all endpoints

        return new CorsFilter(source);
    }
}
