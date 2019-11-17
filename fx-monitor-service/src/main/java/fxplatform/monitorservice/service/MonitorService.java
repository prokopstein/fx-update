package fxplatform.monitorservice.service;

import fxplatform.monitorservice.model.FxPair;

import java.util.List;

public interface MonitorService {
    List<FxPair> checkForChange();
}
