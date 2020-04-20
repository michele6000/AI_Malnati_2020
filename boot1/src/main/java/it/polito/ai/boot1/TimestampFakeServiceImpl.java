package it.polito.ai.boot1;

import org.springframework.stereotype.Service;

//@Primary // serve a dire "prendi me come prioritario nel caso di ambiguita, cioè ho più classi che soddisfano i requisiti"
@Service
public class TimestampFakeServiceImpl implements TimestampService {

    @Override
    public String getTimestamp() {
        return "this is a fake!";

    }
}
