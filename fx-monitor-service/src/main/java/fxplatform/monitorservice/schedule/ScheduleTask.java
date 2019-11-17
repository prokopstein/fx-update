package fxplatform.monitorservice.schedule;

import fxplatform.monitorservice.model.FxPair;
import fxplatform.monitorservice.service.MonitorService;
import fxplatform.monitorservice.stream.MonitorPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Rates get updated and published to event bus here every 15 seconds.
 */

@Slf4j
@Service
public class ScheduleTask {

    private static final long POLL_RATE_MS = 15 * 1000;

    private final MonitorService monitorService;
    private final MonitorPublisher monitorPublisher;

    @Autowired
    public ScheduleTask(final MonitorService monitorService, final MonitorPublisher monitorPublisher) {
        this.monitorService = monitorService;
        this.monitorPublisher = monitorPublisher;
    }

    @Scheduled(fixedDelay = POLL_RATE_MS)
    private void checkRates() {
        try {
            log.info("Trying to get rates update...");
            final List<FxPair> changed = monitorService.checkForChange();

            if (!changed.isEmpty()) {
                log.info("New data arrived...");
                log.info(changed.toString());

                monitorPublisher.publishPairs(changed);
            }
        } catch (final Exception e) {
            log.error("Error in schedule task occurred", e);
        }
    }
}
