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
    private final static Logger logger = LogManager.getLogger(ClientConfig.class);

    private final KameleoonProperties kameleoonProperties;

    public ClientConfig(KameleoonProperties kameleoonProperties) {
        this.kameleoonProperties = kameleoonProperties;
    }

    @Bean
    public KameleoonClient kameleoonClient() throws KameleoonException.SiteCodeIsEmpty, KameleoonException.ConfigCredentialsInvalid {
        try {
            KameleoonClientConfig configuration =
                    new KameleoonClientConfig.Builder()
                            .clientId(kameleoonProperties.getClientId())
                            .clientSecret(kameleoonProperties.getClientSecret())
                            .topLevelDomain(kameleoonProperties.getTopLevelDomain())
                            .build();
            KameleoonClient kameleoonClient = KameleoonClientFactory.create(kameleoonProperties.getSiteCode(), configuration);

            return kameleoonClient;
        } catch (KameleoonException ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
