package com.kameleoon.controller;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = {"Starter Kit API"})
public class VariationController {
    private static final Logger logger = LogManager.getLogger(VariationController.class);

    private final KameleoonClient kameleoonClient;

    public VariationController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    //@formatter:off
    @GetMapping("/Variation")
    public Response getExample(@RequestParam(name = "featureKey") String featureKey,
            HttpServletRequest request, HttpServletResponse response)
            throws KameleoonException.FeatureEnvironmentDisabled,
            KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        // Getting visitor code from request's cookies or generates a new one and adding to response's cookies.
        String visitorCode = kameleoonClient.getVisitorCode(request, response);
        try {
            // Obtaining the variation key for a visitor
            String variationKey = kameleoonClient.getFeatureVariationKey(visitorCode, featureKey);
            return new Response(visitorCode, variationKey);
        } // Handle Kameleoon exceptions
        catch (
            KameleoonException.VisitorCodeInvalid | // Visitor code is invalid
            KameleoonException.FeatureNotFound | // Feature key not found
            KameleoonException.FeatureEnvironmentDisabled exception // Feature is disabled for environment
        ) {
            logger.error("Expected KameleoonException", exception);
            throw exception; // Re-throwing exceptions is generally not recommended; this is for demonstration only.
        } // Handle base exception (for unexpected cases)
        catch (Exception exception) {
            logger.error("Unexpected Exception", exception);
            throw exception; // Re-throwing exceptions is generally not recommended; this is for demonstration only.
        }
    }
    //@formatter:on
}
