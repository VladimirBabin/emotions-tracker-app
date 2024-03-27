//package com.specificgroup.emotionstracker.gateway.filters;
//
//import com.specificgroup.emotionstracker.gateway.services.JwtUtils;
//import com.specificgroup.emotionstracker.gateway.services.RouteValidator;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import static org.springframework.http.HttpStatus.UNAUTHORIZED;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuthFilter implements GatewayFilter {
//
//    private final RouteValidator validator;
//    private final JwtUtils jwtUtils;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//
//        if (validator.isSecured.test(request)) {
//            if (authMissing(request)) {
//                return onError(exchange, UNAUTHORIZED);
//            }
//
//            String bearerToken = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
//
//            if (!bearerToken.isBlank()) {
//                if (jwtUtils.isExpired(bearerToken)) {
//                    return onError(exchange, UNAUTHORIZED);
//                }
//            } else {
//                log.error("No bearer token");
//            }
//        }
//        return chain.filter(exchange);
//    }
//
//
//    private boolean authMissing(ServerHttpRequest request) {
//        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
//    }
//
//
//    private Mono<Void> onError(ServerWebExchange exchange,
//                               HttpStatus httpStatus) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(httpStatus);
//        return response.setComplete();
//    }
//
//
//}
