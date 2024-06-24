package com.kameleoon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kameleoon")
public class KameleoonProperties {

    private String clientId = "clientId";
    private String clientSecret = "clientSecret";
    private String siteCode = "siteCode";
    private String topLevelDomain = "localhost";
}
