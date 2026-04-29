package com.eventrecorder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        
        return builder
                .modules(javaTimeModule)
                .build();
    }
}
