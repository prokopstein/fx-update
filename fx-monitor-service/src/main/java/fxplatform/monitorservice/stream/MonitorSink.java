package fxplatform.monitorservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MonitorSink {
    String OUTPUT = "monitor-out";

    @Output(OUTPUT)
    MessageChannel output();
}
