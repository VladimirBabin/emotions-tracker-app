//package com.specificgroup.emotionstracker.gateway.configuration;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//public class DefaultConfiguration {
//
//    @Value("${spring.cloud.gateway.excludedURLs}")
//    private String urlsStrings;
//
//    @Bean
//    @Qualifier("excludedUrls")
//    public List<String> excludedUrls() {
//        return Arrays.stream(urlsStrings.split(",")).collect(Collectors.toList());
//    }
//
//}
