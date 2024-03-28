//package com.specificgroup.emotionstracker.gateway.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.function.Predicate;
//
//@Service
//public class RouteValidator {
//
//    private List<String> excludedUrls;
//
//    @Autowired
//    public RouteValidator(@Qualifier("excludedUrls") List<String> excludedUrls) {
//        this.excludedUrls = excludedUrls;
//    }
//
//    public Predicate<ServerHttpRequest> isSecured =
//            request -> excludedUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
//}
