package fxplatform.userservice.stream;

import fxplatform.userservice.config.ClientConfig;
import fxplatform.userservice.csv.CsvBuilder;
import fxplatform.userservice.mail.EmailProvider;
import fxplatform.userservice.model.FxPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Event bus listener. Filters events according to client settings and sends appropriate email.
 */

@Component
public class MonitorConsumer {

    private final EmailProvider emailProvider;
    private final ClientConfig clientConfig;

    private final DateFormat fmt = new SimpleDateFormat("yyyymmdd_hhMM");

    @Autowired
    public MonitorConsumer(final EmailProvider emailProvider, final ClientConfig clientConfig) {
        this.emailProvider = emailProvider;
        this.clientConfig = clientConfig;
    }

    @StreamListener(MonitorSink.INPUT)
    public void handleEvent(final List<FxPair> pairs) {
        final List<FxPair> updated = pairs
                                        .stream()
                                        .filter(pair -> clientConfig.getPairs().contains(pair.getName()))
                                        .collect(Collectors.toList());

        if (!updated.isEmpty()) {
            final InputStream attachment = CsvBuilder.build(updated);
            emailProvider.sendEmail(clientConfig.getEmail(),
                    "rate update",
                    "File attached\n",
                    getAttachmentName(),
                    attachment);
        }
    }

    private String getAttachmentName() {
        return String.format("obsval_%s.csv", fmt.format(new Date()));
    }
}
