package com.kameleoon.config;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonClientConfig;
import com.kameleoon.KameleoonClientFactory;
import com.kameleoon.KameleoonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class ClientConfig {
    private static final Logger logger = LogManager.getLogger(ClientConfig.class);

    private final KameleoonProperties kameleoonProperties;

    public ClientConfig(KameleoonProperties kameleoonProperties) {
        this.kameleoonProperties = kameleoonProperties;
    }

    //@formatter:off
    @Bean
    public KameleoonClient kameleoonClient()
            throws KameleoonException.SiteCodeIsEmpty, KameleoonException.ConfigCredentialsInvalid {

        // Read the Kameleoon configuration parameters needed for authentication and site
        // identification
        String clientId = kameleoonProperties.getClientId();
        String clientSecret = kameleoonProperties.getClientSecret();
        String siteCode = kameleoonProperties.getSiteCode();
        // For tests, we're using 'localhost', but on the real website, you need to set your
        // top-level domain, e.g., ".example.com"
        String topLevelDomain = kameleoonProperties.getTopLevelDomain();

        // Kameleoon Integration
        try {
            // Create KameleoonClientConfig
            KameleoonClientConfig configuration = new KameleoonClientConfig.Builder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .topLevelDomain(topLevelDomain)
                    .build();
            // Create KameleoonClient
            return KameleoonClientFactory.create(siteCode, configuration);
        } // Handle Kameleoon exceptions
        catch (KameleoonException.ConfigCredentialsInvalid | KameleoonException.SiteCodeIsEmpty ex) {
            logger.error(String.format("Expected KameleoonException: %s", ex.getMessage()));
            throw ex; // Re-throwing exceptions is generally not recommended; this is for demonstration only.
        }
    }
    //@formatter:on
}
