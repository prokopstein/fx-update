package fxplatform.monitorservice.service.impl;

import fxplatform.monitorservice.config.FxConfig;
import fxplatform.monitorservice.model.FxPair;
import fxplatform.monitorservice.service.MonitorService;
import fxplatform.monitorservice.service.RateFetchService;
import fxplatform.monitorservice.service.dto.FxPairDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/*
 * This service performs rate request and filters only those that have changed since previous run.
 * In the real production last received rates should go to database.
 */

@Service
public class MonitorServiceImpl implements MonitorService {

    private final RateFetchService rateFetchService;
    private final FxConfig fxConfig;

    private final Map<String, BigDecimal> rates = new HashMap<>();

    @Autowired
    public MonitorServiceImpl(final RateFetchService rateFetchService, final FxConfig fxConfig) {
        this.rateFetchService = rateFetchService;
        this.fxConfig = fxConfig;
    }

    @Override
    public List<FxPair> checkForChange() {
        final Set<FxPair> changed = rateFetchService
                .getRates(fxConfig.getPairs())
                .stream()
                .map(this::mapTo)
                .filter(this::equalTo)
                .collect(Collectors.toSet());

        changed.forEach(pair -> {
            rates.put(pair.getName(), pair.getValue());
        });
        return new ArrayList<>(changed);
    }

    private FxPair mapTo(final FxPairDto fxPairDto) {
        String name = fxPairDto.getCode();

        int index = name.lastIndexOf(".FOREX");
        if (index > 0) {
            name = name.substring(0, index);
        }
        return FxPair
                .builder()
                .name(name)
                .value(fxPairDto.getClose())
                .at(Instant.ofEpochSecond(fxPairDto.getTimestamp()))
                .build();
    }

    private boolean equalTo(final FxPair fxPair) {
        final BigDecimal previous = rates.get(fxPair.getName());
        return (previous == null) || previous.compareTo(fxPair.getValue()) != 0;
    }
}
