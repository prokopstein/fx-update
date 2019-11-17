package fxplatform.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "monitor.client")
public class ClientConfig {
    private String email;
    private List<String> pairs = new ArrayList<>();
}
