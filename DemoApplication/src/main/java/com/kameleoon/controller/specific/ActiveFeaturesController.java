package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.types.Variation;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Api(tags = {"Demo API"})
public class ActiveFeaturesController {
    private final static Logger logger = LogManager.getLogger(ActiveFeaturesController.class);

    private final KameleoonClient kameleoonClient;

    public ActiveFeaturesController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/ActiveFeatures")
    public Map<String, Variation> getActiveFeatures(HttpServletRequest request, HttpServletResponse response) throws KameleoonException.VisitorCodeInvalid {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);
        try {
            Map<String, Variation> activeFeatures = kameleoonClient.getActiveFeatures(visitorCode);
            return activeFeatures;
        } catch (KameleoonException.VisitorCodeInvalid ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected Exception", ex);
            throw ex;
        }
    }
}
