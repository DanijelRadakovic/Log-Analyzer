package com.example.logan.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ApplicationConfiguration {

    @Value("${LOG_STORAGE:web-traffic.log}")
    private String logStorage;
}
