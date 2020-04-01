package it.polito.ai.boot1;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Primary
@Service
public class TimestampServiceImp implements TimestampService {

    @Override
    public String getTimestamp() {
        return new Date().toString();
    }
}

// definisco come è implementato il servizio