package com.kameleoon.controller.basic;

import com.kameleoon.KameleoonClient;
import com.kameleoon.KameleoonException;
import com.kameleoon.data.CustomData;
import com.kameleoon.response.RemoteResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The Remote controller shows the basic logic of using the Kameleoon SDK with remote data. It extracts the
 * visitor code from the HTTP request or generates a new one. Then it retrieves the remote data for the visitor
 * and returns a variable value by provided variableKey of assigned variation for a visitor.
 */
@RestController
@Api(tags = {"Demo API"})
public class RemoteController {
    private final static Logger logger = LogManager.getLogger(RemoteController.class);

    private final KameleoonClient kameleoonClient;

    public RemoteController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/Remote")
    @ApiOperation(value = "Remote")
    public CompletableFuture<ResponseEntity<RemoteResponse>> getRemote(
            @RequestParam(name = "featureKey") String featureKey,
            @RequestParam(name = "variableKey") String variableKey,
            HttpServletRequest request, HttpServletResponse response) {

        // Clean `kameleoonVisitorCode` in browser's cookies, if you need to reset the visitorCode
        String visitorCode = kameleoonClient.getVisitorCode(request, response);

        return kameleoonClient.getRemoteVisitorData(visitorCode).thenApplyAsync(remoteData -> {
            try {
                Object variable = kameleoonClient.getFeatureVariable(visitorCode, featureKey, variableKey);

                List<String> customData = remoteData.stream()
                        .filter(data -> data instanceof CustomData)
                        .map(data -> (CustomData) data)
                        .map(cd -> {
                            String values = String.join(", ", cd.getValues());
                            return String.format("CustomData(index: %d, values: %s)", cd.getId(), values);
                        })
                        .collect(Collectors.toList());

                String result = null;
                if (variable != null) {
                    result = variable.toString();
                }
                return ResponseEntity.ok(new RemoteResponse(visitorCode, result, customData));
            } catch (KameleoonException.VisitorCodeInvalid |
                     KameleoonException.FeatureNotFound |
                     KameleoonException.FeatureEnvironmentDisabled |
                     KameleoonException.FeatureVariableNotFound ex) {
                logger.error("Expected KameleoonException", ex);
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                logger.error("Unexpected Exception", ex);
                throw ex;
            }
        });
    }
}
