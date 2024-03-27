//package com.specificgroup.emotionstracker.gateway.configuration;
//
//import com.specificgroup.emotionstracker.gateway.filters.AuthFilter;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RouteConfiguration {
//
//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder,
//                               AuthFilter filter) {
//        return builder.routes()
//                .route("auth-service-route", r -> r.path("/auth/**")
//                        .filters(f ->
//                                f.filter(filter))
//                        .uri("lb://authorization/"))
//                .route("entrylogging-route", r -> r.path("/state/**")
//                        .filters(f ->
//                                f.filter(filter))
//                        .uri("lb://entrylogging/"))
//                .route("alerts-route", r -> r.path("/alerts/**")
//                        .filters(f ->
//                                f.filter(filter))
//                        .uri("lb://alerts/"))
//                .build();
//    }
//}
