package fxplatform.userservice.csv;

import fxplatform.userservice.model.FxPair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestCsvBuilder {
    @Test
    public void testBuild() throws IOException {
        final FxPair fxPair1 = new FxPair();
        fxPair1.setName("EUR");
        fxPair1.setValue(BigDecimal.TEN);

        final FxPair fxPair2 = new FxPair();
        fxPair2.setName("USD");
        fxPair2.setValue(BigDecimal.valueOf(0.037));

        final InputStream stream = CsvBuilder.build(Arrays.asList(fxPair1, fxPair2));
        assertEquals("FOREX, VALUE\nEUR, 10\nUSD, 0.037\n", new String(stream.readAllBytes()));
    }
}
