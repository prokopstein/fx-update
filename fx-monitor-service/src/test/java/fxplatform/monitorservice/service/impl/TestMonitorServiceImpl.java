package fxplatform.monitorservice.service.impl;

import fxplatform.monitorservice.config.FxConfig;
import fxplatform.monitorservice.model.FxPair;
import fxplatform.monitorservice.service.MonitorService;
import fxplatform.monitorservice.service.RateFetchService;
import fxplatform.monitorservice.service.dto.FxPairDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestMonitorServiceImpl {
    @Mock
    private RateFetchService rateFetchService;

    @Test
    public void testCheckForChange() {
        when(rateFetchService.getRates(anyList())).thenReturn(getRates());

        final FxConfig fxConfig = new FxConfig();
        fxConfig.getPairs().add("EUR");

        final MonitorService monitorService = new MonitorServiceImpl(rateFetchService, fxConfig);

        final List<FxPair> pairs1 = monitorService.checkForChange();
        assertNotNull(pairs1);
        assertEquals(1, pairs1.size());
        assertNotNull(pairs1.get(0));
        assertEquals("EUR", pairs1.get(0).getName());

        final List<FxPair> pairs2 = monitorService.checkForChange();
        assertNotNull(pairs2);
        assertEquals(0, pairs2.size());
    }

    private List<FxPairDto> getRates() {
        final Function<String, FxPairDto> createPair = (final String code) -> {
            final FxPairDto fxPairDto = new FxPairDto();
            fxPairDto.setCode(code.concat(".FOREX"));
            fxPairDto.setClose(BigDecimal.ONE);
            fxPairDto.setTimestamp(Instant.now().getEpochSecond());
            return fxPairDto;
        };
        return Arrays.asList(createPair.apply("EUR"));
    }
}
