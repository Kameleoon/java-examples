package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.FeatureVariableResponse;
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
public class FeatureVariableController {
    private final static Logger logger = LogManager.getLogger(FeatureVariableController.class);

    private final KameleoonClient kameleoonClient;

    public FeatureVariableController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/FeatureVariable")
    public FeatureVariableResponse getFeatureVariable(
            @RequestParam("featureKey") String featureKey,
            @RequestParam("variableKey") String variableKey,
            HttpServletRequest request, HttpServletResponse response) throws KameleoonException.FeatureEnvironmentDisabled,
            KameleoonException.FeatureVariableNotFound, KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {

        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);

        try {
            Object variable = kameleoonClient.getFeatureVariable(visitorCode, featureKey, variableKey);
            String result = null;
            if (variable != null) {
                result = variable.toString();
            }
            return new FeatureVariableResponse(visitorCode, result);
        } catch (KameleoonException.VisitorCodeInvalid |
                 KameleoonException.FeatureNotFound |
                 KameleoonException.FeatureVariableNotFound |
                 KameleoonException.FeatureEnvironmentDisabled ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        } catch (Exception exception) {
            logger.error("Unexpected Exception", exception);
            throw new RuntimeException("Unexpected Exception", exception);
        }
    }
}
