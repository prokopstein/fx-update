package fxplatform.monitorservice.config;

import fxplatform.monitorservice.stream.MonitorSink;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamConfig {
    @MockBean
    private MonitorSink monitorSink;
}
