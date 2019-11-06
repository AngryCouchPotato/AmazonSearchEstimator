package com.kaa.sellics.service;


import com.kaa.sellics.model.EstimationRequest;
import com.kaa.sellics.model.EstimationResult;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EstimationServiceImplTest {

  @Test(expected = IllegalArgumentException.class)
  public void estimate_NullableKeywordGiven_ShouldThrowException() {
    // Given
    EstimationRequest request = new EstimationRequest(null, new StopWatch());
    EstimationService estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();

    // When
    estimationService.estimate(request);
  }

  @Test(expected = IllegalArgumentException.class)
  public void estimate_EmptyKeywordGiven_ShouldThrowException() {
    // Given
    EstimationRequest request = new EstimationRequest("         ", new StopWatch());
    EstimationService estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();

    // When
    estimationService.estimate(request);
  }

  @Test
  public void estimate_VeryPopularKeywordGiven_ShouldEstimateScore100() {
    // Given
    String keyword = "iphone charger";
    EstimationRequest request = new EstimationRequest(keyword, new StopWatch());
    EstimationServiceImpl estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();
    when(estimationService.getCompletions(any())).thenReturn(getSetOfValues());

    // When
    EstimationResult result = estimationService.estimate(request);

    // Then
    assertNotNull(result);
    assertEquals(100, result.getScore());
    verify(estimationService, times(3)).getCompletions(any());
  }

  @Test
  public void estimate_MiddlePopularKeywordGiven_ShouldEstimateScore78() {
    // Given
    String keyword = "iphone charger";
    EstimationRequest request = new EstimationRequest(keyword, new StopWatch());
    EstimationServiceImpl estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();
    when(estimationService.getCompletions("iphone ")).thenReturn(getSetOfValues());

    // When
    EstimationResult result = estimationService.estimate(request);

    // Then
    assertNotNull(result);
    assertEquals(57, result.getScore());
    verify(estimationService, times(4)).getCompletions(any());
  }

  @Test
  public void estimate_NotPopularKeywordGiven_ShouldEstimateScore0() {
    // Given
    String keyword = "iphone charger one two three four five six seven eight nine ten";
    EstimationRequest request = new EstimationRequest(keyword, new StopWatch());
    EstimationServiceImpl estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.estimate(request)).thenCallRealMethod();
    when(estimationService.getCompletions(any())).thenReturn(getSetOfValues());

    // When
    EstimationResult result = estimationService.estimate(request);

    // Then
    assertNotNull(result);
    assertEquals(0, result.getScore());
    verify(estimationService, times(6)).getCompletions(any());
  }


  @Test
  public void parse_CorrectJSonStringGiven_ShouldParseSetWithCorrectWords() {
    // Given
    String json = getJson();
    EstimationRequest request = new EstimationRequest(null, new StopWatch());
    EstimationServiceImpl estimationService = Mockito.mock(EstimationServiceImpl.class);
    when(estimationService.parse(json)).thenCallRealMethod();

    // When
    Set<String> parsed = estimationService.parse(json);

    // Then
    assertNotNull(parsed);
    assertEquals(10, parsed.size());
    for(String value : getSetOfValues()) {
      assertTrue(parsed.contains(value));
    }
  }

  private Set<String> getSetOfValues(){
    Set<String> set = new HashSet<>();
    set.add("iphone charger cable");
    set.add("iphone charger");
    set.add("iphone 11 case");
    set.add("iphone cable");
    set.add("iphone 11 pro max case");
    set.add("iphone 11 screen protector");
    set.add("iphone 11 pro case");
    set.add("iphone xr cases");
    set.add("iphone 8 case");
    set.add("iphone 7 case");
    return set;
  }

  private String getJson() {
    return "[\n" +
            "  \"iphone\",\n" +
            "  [\n" +
            "    \"iphone charger cable\",\n" +
            "    \"iphone charger\",\n" +
            "    \"iphone 11 case\",\n" +
            "    \"iphone cable\",\n" +
            "    \"iphone 11 pro max case\",\n" +
            "    \"iphone 11 screen protector\",\n" +
            "    \"iphone 11 pro case\",\n" +
            "    \"iphone xr cases\",\n" +
            "    \"iphone 8 case\",\n" +
            "    \"iphone 7 case\"\n" +
            "  ],\n" +
            "  [\n" +
            "    {\n" +
            "      \"nodes\": [\n" +
            "        {\n" +
            "          \"name\": \"Electronics\",\n" +
            "          \"alias\": \"electronics\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {},\n" +
            "    {}\n" +
            "  ],\n" +
            "  [],\n" +
            "  \"6F3D9HHUQ4VK\"\n" +
            "]";
  }

  @Test
  public void buildUri_CorrectKeywordGiven_ShouldCreateURI() {
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