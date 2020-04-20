package it.polito.ai.mavenExample;

import java.time.LocalDateTime;

public class Message {
    private String msg;
    private LocalDateTime date;

    public Message(String msg, LocalDateTime date) {
        this.msg = msg;
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
