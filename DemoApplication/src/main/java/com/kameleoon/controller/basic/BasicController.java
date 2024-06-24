package com.kameleoon.controller.basic;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.BasicResponse;
import com.kameleoon.data.CustomData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The BasicController shows the basic logic of using Kameleoon SDK. It extracts the visitor code from the HTTP
 * request or generates a new one. Then it adds a CustomData and returns the resultant variation key for a visitor.
 */
@RestController
@Api(tags = {"Demo API"})
public class BasicController {
    private final static Logger logger = LogManager.getLogger(BasicController.class);

    private final KameleoonClient kameleoonClient;

    public BasicController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/Basic")
    @ApiOperation(value = "Basic")
    public BasicResponse getBasic(
            @RequestParam(name = "index") int index,
            @RequestParam(name = "value") String value,
            @RequestParam(name = "featureKey") String featureKey,
            HttpServletRequest request, HttpServletResponse response) throws KameleoonException.FeatureEnvironmentDisabled, KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {

        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);

        try {
            kameleoonClient.addData(visitorCode, new CustomData(index, value));
            String variationKey = kameleoonClient.getFeatureVariationKey(visitorCode, featureKey);

            return new BasicResponse(visitorCode, variationKey);
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
