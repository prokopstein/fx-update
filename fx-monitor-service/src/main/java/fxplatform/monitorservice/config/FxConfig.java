package fxplatform.monitorservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/*
 * Service config.
 * In the real production pairs should go to database, apiKey to kubernetes secrets or so.
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "monitor.fx")
public class FxConfig {
    private String apiKey;
    private List<String> pairs = new ArrayList<>();
}
