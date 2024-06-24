package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import com.kameleoon.data.CustomData;
import com.kameleoon.data.Data;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"Demo API"})
public class RemoteVisitorDataController {
    private final static Logger logger = LogManager.getLogger(RemoteVisitorDataController.class);

    private final KameleoonClient kameleoonClient;

    public RemoteVisitorDataController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/RemoteVisitorData")
    public List<String> getRemoteVisitorData(HttpServletRequest request, HttpServletResponse response) {
        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);

        try {
            List<Data> remoteData = kameleoonClient.getRemoteVisitorData(visitorCode).get();
            return remoteData.stream()
                    .map(cd -> {
                        if (cd instanceof CustomData) {
                            CustomData customData = (CustomData) cd;
                            String values = String.join(", ", customData.getValues());
                            return String.format("CustomData(index: %d, values: %s)", customData.getId(), values);
                        }
                        return null;
                    })
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            logger.error("Unexpected Exception", exception);
            throw new RuntimeException("Unexpected Exception", exception);
        }
    }
}
