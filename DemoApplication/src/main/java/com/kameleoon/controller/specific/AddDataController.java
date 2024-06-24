package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.response.AddDataResponse;
import com.kameleoon.data.CustomData;
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
public class AddDataController {
    private final static Logger logger = LogManager.getLogger(AddDataController.class);

    private final KameleoonClient kameleoonClient;

    public AddDataController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/AddData")
    public AddDataResponse addData(@RequestParam("index") int index, @RequestParam("value") String value,
                                                   HttpServletRequest request, HttpServletResponse response) throws KameleoonException.VisitorCodeInvalid {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);
        try {
            kameleoonClient.addData(visitorCode, new CustomData(index, value));
            kameleoonClient.flush(visitorCode);
            return new AddDataResponse(visitorCode, String.format("CustomData(index: %d, value: %s) was added and flushed successfully", index, value));
        } catch (KameleoonException.VisitorCodeInvalid ex) {
            logger.error("Expected KameleoonException", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected Exception", ex);
            throw ex;
        }
    }
}
