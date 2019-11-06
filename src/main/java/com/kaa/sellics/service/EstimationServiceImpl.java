package com.kaa.sellics.service;

import com.kaa.sellics.model.EstimationRequest;
import com.kaa.sellics.model.EstimationResult;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EstimationServiceImpl implements EstimationService{

    private static final Logger logger = LoggerFactory.getLogger(EstimationServiceImpl.class);

    private static final String AMAZON_COMPLETION_URL = "https://completion.amazon.com/search/complete";
    private RestTemplate restTemplate;

    public EstimationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public EstimationResult estimate(EstimationRequest request) {
        if(request.getKeyword() == null) {
            throw new IllegalArgumentException("Keyword could not be null.");
        }
        String keyword = request.getKeyword().trim();
        if(keyword.length() == 0) {
            throw new IllegalArgumentException("Keyword could not be empty.");
        }
        return new EstimationResult(keyword, doEstimate(request));
    }

    private int doEstimate(EstimationRequest request) {
        String keyword = request.getKeyword();
        StopWatch timer = request.getTimer();
        int result = 0;
        int left = 0, right = keyword.length() - 1, counter = 0;
        while(left <= right) {
            if( counter > 0 && (timer.getTime() + ((double)timer.getTime() / counter) >= 10_000.00) ) {
                return result;
            }
            int middle = left + (right - left) / 2;
            String subString = keyword.substring(0, middle + 1);
            if(getCompletions(subString).contains(keyword)) {
                result = ((keyword.length() - middle) * 100) / keyword.length();
                right = middle - 1;
            } else {
                left = middle + 1;
            }
            counter++;
        }
        return result;
    }

    Set<String> getCompletions(String word) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                buildUri(word),
                String.class
        );
        return parse(responseEntity.getBody());
    }

    Set<String> parse(String json) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        List<Object> objects = parser.parseList(json);
        return ((ArrayList<Object>)objects.get(1))
                .stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toSet());
    }

    URI buildUri(String word) {
        UriComponentsBuilder uriComponentBuilder = UriComponentsBuilder
                .fromUriString(AMAZON_COMPLETION_URL);
        uriComponentBuilder.queryParam("search-alias", "aps");
        uriComponentBuilder.queryParam("client", "amazon-search-ui");
        uriComponentBuilder.queryParam("mkt", "1");
        uriComponentBuilder.queryParam("q", word);

        URI uri = uriComponentBuilder.build().toUri();
        logger.debug(String.format("URI = %s", uri.toString()));
        return uri;
    }

}
