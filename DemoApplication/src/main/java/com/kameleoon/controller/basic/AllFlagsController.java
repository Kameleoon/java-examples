package com.kameleoon.controller.basic;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.AllFlagsResponse;
import com.kameleoon.response.FeatureInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * The AllFlags controller shows the basic logic of using the Kameleoon SDK. It extracts the visitor code from
 * the HTTP request or generates a new one. After that it checks all available feature flags and returns a bool
 * value indicating whether flag is active for a visitor or not.
 */
@RestController
@Api(tags = {"Demo API"})
public class AllFlagsController {
    private final static Logger logger = LogManager.getLogger(AllFlagsController.class);

    private final KameleoonClient kameleoonClient;

    public AllFlagsController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/AllFlags")
    @ApiOperation(value = "AllFlags")
    public AllFlagsResponse getAllFlags(HttpServletRequest request, HttpServletResponse response) throws KameleoonException.VisitorCodeInvalid, KameleoonException.FeatureNotFound {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);

        try {
            List<String> allFlags = kameleoonClient.getFeatureList();
            List<FeatureInfo> featuresInfo = new ArrayList<>();
            for (String flag : allFlags) {
                featuresInfo.add(new FeatureInfo(flag, kameleoonClient.isFeatureActive(visitorCode, flag)));
            }

            return new AllFlagsResponse(visitorCode, featuresInfo);
        } catch (KameleoonException.VisitorCodeInvalid |
                 KameleoonException.FeatureNotFound ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        }
        catch (Exception ex) {
            logger.error("Unexpected Exception", ex);
            throw ex;
        }
    }
}
