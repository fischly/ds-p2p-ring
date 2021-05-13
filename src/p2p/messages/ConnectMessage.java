package p2p.messages;

import java.io.Serializable;

public class ConnectMessage implements Message, Serializable {
    @Override
    public String toString() {
        return "ConnectMessage{}";
    }
}
