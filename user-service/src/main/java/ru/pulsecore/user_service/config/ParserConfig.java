package ru.pulsecore.user_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua_parser.Parser;


@Configuration
public class ParserConfig {

    @Bean
    public Parser uaParser() {
        return new Parser();
    }
}