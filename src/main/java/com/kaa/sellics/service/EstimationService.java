package com.kaa.sellics.service;

import com.kaa.sellics.model.EstimationRequest;
import com.kaa.sellics.model.EstimationResult;

public interface EstimationService {

    EstimationResult estimate(EstimationRequest estimationRequest);

}
