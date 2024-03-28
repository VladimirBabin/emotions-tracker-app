//package com.specificgroup.emotionstracker.gateway.filters;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.specificgroup.emotionstracker.gateway.dto.ConnValidationResponse;
//import com.specificgroup.emotionstracker.gateway.dto.ExceptionResponseDto;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.function.Predicate;
//
//import static org.springframework.http.HttpStatus.BAD_GATEWAY;
//
//@Slf4j
//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
//
//    public static final String AUTH_FAILED_CODE = "JWT Authentication Failed";
//    public static final String AUTHORIZATION_VALIDATION_ENDPOINT = "lb://authorization/validateToken";
//    private List<String> excludedUrls;
//    private final WebClient.Builder webClientBuilder;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public AuthenticationFilter(@Qualifier("excludedUrls") List<String> excludedUrls,
//                                WebClient.Builder webClientBuilder,
//                                ObjectMapper objectMapper) {
//        super(Config.class);
//        this.excludedUrls = excludedUrls;
//        this.webClientBuilder = webClientBuilder;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            log.info("URL is - " + request.getURI().getPath());
//            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            log.info("Bearer Token: "+ bearerToken);
//
//            if(isSecured.test(request)) {
//                return webClientBuilder.build().get()
//                        .uri(AUTHORIZATION_VALIDATION_ENDPOINT)
//                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
//                        .retrieve().bodyToMono(ConnValidationResponse.class)
//                        .map(response -> {
//                                exchange.getRequest().mutate().header("login", response.getLogin());
//                                return exchange;
//                        })
//                        .flatMap(chain::filter)
//                        .onErrorResume(error -> {
//                            log.info("Error Happened");
//                            HttpStatusCode errorCode;
//                            String errorMsg = "";
//                            if (error instanceof WebClientResponseException webClientException) {
//                                errorCode = webClientException.getStatusCode();
//                                errorMsg = webClientException.getStatusText();
//                            } else {
//                                errorCode = BAD_GATEWAY;
//                                errorMsg = BAD_GATEWAY.getReasonPhrase();
//                            }
//                            return onError(exchange, String.valueOf(errorCode.value()) ,errorMsg, AUTH_FAILED_CODE, errorCode);
//                        });
//            }
//
//            return chain.filter(exchange);
//        };
//    }
//
//    private Mono<Void> onError(ServerWebExchange exchange,
//                               String errCode,
//                               String err,
//                               String errDetails,
//                               HttpStatusCode httpStatus) {
//        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(httpStatus);
//        try {
//            response.getHeaders().add("Content-Type", "application/json");
//            ExceptionResponseDto data = new ExceptionResponseDto(errCode, err, errDetails, LocalDateTime.now());
//            byte[] byteData = objectMapper.writeValueAsBytes(data);
//            return response.writeWith(Mono.just(byteData).map(dataBufferFactory::wrap));
//        } catch (JsonProcessingException e) {
//            log.error(Arrays.toString(e.getStackTrace()));
//        }
//        return response.setComplete();
//    }
//
//
//    public Predicate<ServerHttpRequest> isSecured =
//            request -> excludedUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
//
//    @NoArgsConstructor
//    public static class Config {
//    }
//}
