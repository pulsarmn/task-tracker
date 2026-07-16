package org.pulsar.tracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;


@Configuration
public class ApplicationConfiguration {

    @Bean
    Clock defaultClock() {
        return Clock.systemUTC();
    }
}
