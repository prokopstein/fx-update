package fxplatform.monitorservice.stream.impl;

import fxplatform.monitorservice.model.FxPair;
import fxplatform.monitorservice.stream.MonitorPublisher;
import fxplatform.monitorservice.stream.MonitorSink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

/*
 * Event bus publisher.
 */

@Component
public class MonitorPublisherImpl implements MonitorPublisher {

    private final MonitorSink monitorSink;

    @Autowired
    public MonitorPublisherImpl(final MonitorSink monitorSink) {
        this.monitorSink = monitorSink;
    }

    @Override
    public void publishPairs(final List<FxPair> pairs) {
        publish(pairs);
    }

    private void publish(final Object payload) {
        monitorSink.output().send(MessageBuilder
                .withPayload(payload)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
