package p2p.messages;

import p2p.Peer;

import java.io.Serializable;

public class ConnectMessage implements Message, Serializable {
    private Peer from;

    public ConnectMessage(Peer from) {
        this.from = from;
    }

    @Override
    public Peer getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "ConnectMessage{}";
    }
}
