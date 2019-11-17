package fxplatform.userservice.csv;

import fxplatform.userservice.model.FxPair;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/*
 * Build csv stream from list of currency rate data.
 */

public class CsvBuilder {
    public static InputStream build(final List<FxPair> pairs) {
        final StringBuilder sb = new StringBuilder();
        sb.append("FOREX, VALUE\n");

        pairs.stream().forEach(pair -> {
            sb.append(String.format("%s, %s\n", pair.getName(), pair.getValue()));
        });
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
