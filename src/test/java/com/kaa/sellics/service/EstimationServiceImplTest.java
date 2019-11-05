package com.kaa.sellics.service;


import com.kaa.sellics.model.EstimationRequest;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EstimationServiceImplTest {

  @Test(expected = IllegalArgumentException.class)
  public void estimateWithNullableKeyword() {
    // Given
    EstimationRequest request = new EstimationRequest(null, new StopWatch());
    EstimationService estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();

    // When
    estimationService.estimate(request);
  }

  @Test(expected = IllegalArgumentException.class)
  public void estimateWithEmptyKeyword() {
    // Given
    EstimationRequest request = new EstimationRequest("         ", new StopWatch());
    EstimationService estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();

    // When
    estimationService.estimate(request);
  }

  @Test
  public void buildUri() {
    // Given
    String expected = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q=iphone";
    String word = "iphone";
    EstimationServiceImpl estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.buildUri(word)).thenCallRealMethod();

    // When
    URI uri = estimationService.buildUri(word);

    // Then
    assertNotNull(uri);
    assertEquals(expected, uri.toString());
  }
}