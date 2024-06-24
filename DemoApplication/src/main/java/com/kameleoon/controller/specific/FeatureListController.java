package com.kameleoon.controller.specific;

import com.kameleoon.KameleoonClient;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"Demo API"})
public class FeatureListController {
    private final static Logger logger = LogManager.getLogger(FeatureListController.class);

    private final KameleoonClient kameleoonClient;

    public FeatureListController(KameleoonClient kameleoonClient) {
        this.kameleoonClient = kameleoonClient;
    }

    @GetMapping("/FeatureList")
    public List<String> getFeatureList() {
        return kameleoonClient.getFeatureList();
    }
}
