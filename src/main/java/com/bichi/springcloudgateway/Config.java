package com.bichi.springcloudgateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class Config {
    //ref https://github.com/coded-tribe/springCloudGateway/blob/master/src/main/java/com/codedTribe/SpringCloudGateway/config/GatewayConfig.java
    //ref https://cloud.spring.io/spring-cloud-gateway/reference/html/
    @Bean
    public RouteLocator myRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route("exchange",p->p
                        .path("/exchange/**")
                        .uri("http://localhost:8080"))
                .route("entity",p->p
                        .path("/entity/**")
                        .uri("http://localhost:8080"))
                .route("object",p->p
                        .path("/object/**")
                        .filters(f->f.circuitBreaker(c->c.setName("Object").setFallbackUri("/defaultFallback")))
                        .uri("http://localhost:8080"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer(){
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(2)).build())
                .build());
    }
}
