package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.FeatureActiveResponse;
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
public class FeatureActiveController {
    private final static Logger logger = LogManager.getLogger(FeatureActiveController.class);

    private final KameleoonClient kameleoonClient;

    public FeatureActiveController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/FeatureActive")
    public FeatureActiveResponse getFeatureActive(@RequestParam("featureKey") String featureKey,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) throws KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);
        try {
            boolean active = kameleoonClient.isFeatureActive(visitorCode, featureKey);
            return new FeatureActiveResponse(visitorCode, active);
        } catch (KameleoonException.VisitorCodeInvalid |
                 KameleoonException.FeatureNotFound ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected Exception", ex);
            throw ex;
        }
    }
}
