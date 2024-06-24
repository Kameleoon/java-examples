package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.FeatureVariationKeyResponse;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = {"Demo API"})
public class FeatureVariationKeyController {
    private final static Logger logger = LogManager.getLogger(FeatureVariationKeyController.class);

    private final KameleoonClient kameleoonClient;

    public FeatureVariationKeyController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/FeatureVariationKey")
    public FeatureVariationKeyResponse getFeatureVariationKey(@RequestParam("featureKey") String featureKey,
                                                              HttpServletRequest request, HttpServletResponse response
    ) throws KameleoonException.FeatureEnvironmentDisabled, KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {

        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);

        try {
            String variationKey = kameleoonClient.getFeatureVariationKey(visitorCode, featureKey);
            return new FeatureVariationKeyResponse(visitorCode, variationKey);
        } catch (KameleoonException.VisitorCodeInvalid |
                 KameleoonException.FeatureNotFound |
                 KameleoonException.FeatureEnvironmentDisabled ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        } catch (Exception exception) {
            logger.error("Unexpected Exception", exception);
            throw new RuntimeException("Unexpected Exception", exception);
        }
    }
}
