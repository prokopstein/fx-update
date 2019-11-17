package fxplatform.userservice.config;

import fxplatform.userservice.stream.MonitorSink;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding({MonitorSink.class})
public class StreamConfig {}
