package fxplatform.monitorservice.service;

import fxplatform.monitorservice.service.dto.FxPairDto;

import java.util.List;

public interface RateFetchService {
    List<FxPairDto> getRates(final List<String> codes);
}
