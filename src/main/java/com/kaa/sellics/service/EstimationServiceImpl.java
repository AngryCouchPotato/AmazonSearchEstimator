package com.kaa.sellics.service;

import com.kaa.sellics.model.EstimationResult;
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
    private RestTemplate restTemplate;

    private static final String AMAZON_COMPLETION_URL = "https://completion.amazon.com/search/complete";

    public EstimationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public EstimationResult estimate(String keyword) {
        if(keyword == null) {
            throw new IllegalArgumentException("Keyword could not be null.");
        }
        keyword = keyword.trim();
        if(keyword.length() == 0) {
            throw new IllegalArgumentException("Keyword could not be empty.");
        }
        return new EstimationResult(keyword, doEstimate(keyword));
    }

    private int doEstimate(String keyword) {
        int result = 0;
        int left = 0, right = keyword.length() - 1;
        while(left <= right) {
            int middle = left + (right - 1) / 2;
            String subString = keyword.substring(0, middle + 1);
            if(getCompletions(subString).contains(keyword)) {
                result = ((keyword.length() - middle) * 100) / keyword.length();
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return result;
    }

    private Set<String> getCompletions(String word) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                buildUri(word),
                String.class
        );
        String json = responseEntity.getBody();
        JsonParser parser  = JsonParserFactory.getJsonParser();
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
