package fxplatform.monitorservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fxplatform.monitorservice.config.FxConfig;
import fxplatform.monitorservice.service.RateFetchService;
import fxplatform.monitorservice.service.dto.FxPairDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RateFetchServiceImpl implements RateFetchService {

    private final RestTemplate restTemplate;
    private final FxConfig fxConfig;
    private final ObjectMapper mapper;

    @Autowired
    public RateFetchServiceImpl(final RestTemplate restTemplate,
                                final FxConfig fxConfig,
                                final ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.fxConfig = fxConfig;
        this.mapper = mapper;
    }

    @Override
    public List<FxPairDto> getRates(final List<String> codes) {
        final URI uri = buildUri(codes);

        final String content = restTemplate.getForObject(uri, String.class);
        return Arrays.asList(parseResponse(content));
    }

    private URI buildUri(final List<String> codes) {
        final String firstPair = codes
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Codes list is empty"));

        final String sParam = codes
                .stream()
                .skip(1)
                .map(code -> code.concat(".FOREX"))
                .collect(Collectors.joining(","));

        return UriComponentsBuilder
                .fromUriString("https://eodhistoricaldata.com/api/real-time/{pair}")
                .queryParam("api_token", fxConfig.getApiKey())
                .queryParam("fmt", "json")
                .queryParam("s", sParam)
                .build(firstPair.concat(".FOREX"));
    }

    private FxPairDto[] parseResponse(final String content) {
        try {
            final JsonNode node = mapper.readTree(content);

            if (node.isArray()) {
                return mapper.treeToValue(node, FxPairDto[].class);
            }
            return new FxPairDto[] { mapper.treeToValue(node, FxPairDto.class) };
        } catch (final IOException e) {
            log.error("Bad server response", e);
            throw new RuntimeException("Bad server response", e);
        }
    }
}
