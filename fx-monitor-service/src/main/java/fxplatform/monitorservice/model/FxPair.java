package fxplatform.monitorservice.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

/*
 * This model is sent via event bus (rabbit or kafka) and represents currency pair rate update.
 */

@Value
@Builder
public class FxPair {
    String name;
    BigDecimal value;
    Instant at;
}
