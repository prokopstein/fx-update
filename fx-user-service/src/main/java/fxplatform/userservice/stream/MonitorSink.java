package fxplatform.userservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MonitorSink {
    String INPUT = "monitor-in";

    @Input(INPUT)
    SubscribableChannel input();
}
