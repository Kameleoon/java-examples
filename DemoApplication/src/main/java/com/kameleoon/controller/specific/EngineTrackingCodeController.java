package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.EngineTrackingCodeResponse;
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
public class EngineTrackingCodeController {
    private final static Logger logger = LogManager.getLogger(EngineTrackingCodeController.class);

    private final KameleoonClient kameleoonClient;

    public EngineTrackingCodeController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/EngineTrackingCode")
    public EngineTrackingCodeResponse getEngineTrackingCode(@RequestParam("featureKey") String featureKey,
                                                                            HttpServletRequest request,
                                                                            HttpServletResponse response) throws KameleoonException.FeatureEnvironmentDisabled, KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);
        try {
            String variationKey = kameleoonClient.getFeatureVariationKey(visitorCode, featureKey);
            String engineTrackingCode = kameleoonClient.getEngineTrackingCode(visitorCode);
            return new EngineTrackingCodeResponse(visitorCode, variationKey, engineTrackingCode);
        } catch (KameleoonException.VisitorCodeInvalid |
                 KameleoonException.FeatureNotFound |
                 KameleoonException.FeatureEnvironmentDisabled ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected Exception", ex);
            throw ex;
        }
    }
}
