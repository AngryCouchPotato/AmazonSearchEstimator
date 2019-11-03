package com.kaa.sellics.controller;

import com.kaa.sellics.model.EstimationResult;
import com.kaa.sellics.service.EstimationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController()
public class EstimationController {

    private static final Logger logger = LoggerFactory.getLogger(EstimationController.class);

    private EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @RequestMapping(value = "/estimate", method = RequestMethod.GET)
    public EstimationResult estimate(@RequestParam String keyword) {
        return estimationService.estimate(keyword);
    }
}
