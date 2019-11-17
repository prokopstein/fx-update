package fxplatform.monitorservice.stream;

import fxplatform.monitorservice.model.FxPair;

import java.util.List;

public interface MonitorPublisher {
    void publishPairs(final List<FxPair> pairs);
}
