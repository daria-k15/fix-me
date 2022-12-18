package com.school_21.fixme.utils.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Socket;
import java.util.List;

@Data
public class Message {
    private boolean valid = true;
    private Socket socket;

    protected String rawMessage;

    private List<String> rawParts;

    public Message(String rawMessage) {
        this.rawMessage = rawMessage;
        this.rawParts = List.of(this.rawMessage.split("\\|"));
    }

    public String get(String key) {
        for (String part : rawParts) {
            if (part.startsWith(key + "=")) {
                return part.substring(key.length() + 1, part.length());
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return rawMessage;
    }
}
