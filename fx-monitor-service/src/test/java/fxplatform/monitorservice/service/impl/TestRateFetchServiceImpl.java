package fxplatform.monitorservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fxplatform.monitorservice.service.RateFetchService;
import fxplatform.monitorservice.service.dto.FxPairDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestRateFetchServiceImpl {
    @Autowired
    private RateFetchService rateFetchService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setUp() {
        RestGatewaySupport restGatewaySupport = new RestGatewaySupport();
        restGatewaySupport.setRestTemplate(restTemplate);
        mockRestServiceServer = MockRestServiceServer.createServer(restGatewaySupport);
    }

    @Test
    public void testGetRatesOne() {
        final List<String> codes = Arrays.asList("EUR");

        mockRestServiceServer
                .expect(ExpectedCount.once(), requestTo(expectedUrl(codes)))
                .andRespond(withSuccess(expectedResponse(codes), MediaType.APPLICATION_JSON));

        final List<FxPairDto> pairs = rateFetchService.getRates(codes);
        assertNotNull(pairs);
        assertEquals(1, pairs.size());

        final FxPairDto fxPairDto = pairs.get(0);
        assertNotNull(fxPairDto);
        assertEquals("EUR.FOREX", fxPairDto.getCode());
        assertEquals(BigDecimal.ONE, fxPairDto.getClose());
    }

    @Test
    public void testGetRatesTwo() {
        final List<String> codes = Arrays.asList("EUR", "USD");

        mockRestServiceServer
                .expect(ExpectedCount.once(), requestTo(expectedUrl(codes)))
                .andRespond(withSuccess(expectedResponse(codes), MediaType.APPLICATION_JSON));

        final List<FxPairDto> pairs = rateFetchService.getRates(codes);
        assertNotNull(pairs);
        assertEquals(2, pairs.size());

        final FxPairDto fxPairDto1 = pairs.get(0);
        assertNotNull(fxPairDto1);
        assertEquals("EUR.FOREX", fxPairDto1.getCode());
        assertEquals(BigDecimal.ONE, fxPairDto1.getClose());

        final FxPairDto fxPairDto2 = pairs.get(1);
        assertNotNull(fxPairDto2);
        assertEquals("USD.FOREX", fxPairDto2.getCode());
        assertEquals(BigDecimal.ONE, fxPairDto2.getClose());
    }

    private String expectedUrl(final List<String> codes) {
        final String fCode = codes.get(0).concat(".FOREX");

        final String sParam = codes
                .stream()
                .skip(1)
                .map(code -> code.concat(".FOREX"))
                .collect(Collectors.joining(","));

        return String.format("https://eodhistoricaldata.com/api/real-time/%s?api_token=api-key&fmt=json&s=%s",
                fCode, sParam);
    }

    private String expectedResponse(final List<String> codes) {
        final Function<String, FxPairDto> createPair = (final String code) -> {
            final FxPairDto fxPairDto = new FxPairDto();
            fxPairDto.setCode(code.concat(".FOREX"));
            fxPairDto.setClose(BigDecimal.ONE);
            fxPairDto.setTimestamp(Instant.now().getEpochSecond());
            return fxPairDto;
        };

        final List<FxPairDto> pairs = codes.stream().map(code -> createPair.apply(code)).collect(Collectors.toList());
        if (pairs.size() == 1) {
            return objectMapper.valueToTree(pairs.get(0)).toString();
        }
        return objectMapper.valueToTree(pairs).toString();
    }
}
