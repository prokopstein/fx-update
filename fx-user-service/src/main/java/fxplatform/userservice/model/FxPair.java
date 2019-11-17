package fxplatform.userservice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class FxPair {
    String name;
    BigDecimal value;
    Instant at;
}
