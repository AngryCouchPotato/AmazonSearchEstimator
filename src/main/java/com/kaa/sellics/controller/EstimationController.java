package com.kaa.sellics.controller;

import com.kaa.sellics.model.EstimationRequest;
import com.kaa.sellics.model.EstimationResult;
import com.kaa.sellics.service.EstimationService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController()
public class EstimationController {

    private static final Logger logger = LoggerFactory.getLogger(EstimationController.class);

    private EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @RequestMapping(value = "/estimate", method = RequestMethod.GET)
    public EstimationResult estimate(@RequestParam @Nullable String keyword) {
        StopWatch timer = new StopWatch();
        timer.start();
        EstimationResult result = estimationService.estimate(new EstimationRequest(keyword, timer));
        logger.info(String.format("Estimation method executed %s milliseconds", timer.getTime()));
        return result;
    }
}
