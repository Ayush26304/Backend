package com.cts.gatedemo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth", r -> r.path("/api/auth/**")
                .uri("lb://AUTH"))
            .route("User", r -> r.path("/api/user/**")
                .uri("lb://USER"))
            .route("catalog", r -> r.path("/api/catalog/product/**")
                .uri("lb://CATALOG"))
            .route("catalog", r -> r.path("/api/catalog/category/**")
                .uri("lb://CATALOG"))
            .route("cart", r -> r.path("/api/cart/**")
                    .uri("lb://CART"))
            .route("order", r -> r.path("/api/order/**")
                    .uri("lb://ORDER"))
            .route("wishlist", r -> r.path("/api/wishlist/**")
                    .uri("lb://WISHLIST"))
            .route("test", r -> r.path("/t/**")
                    .uri("lb://TEST"))
            .build();
    }
}

