package p2p.messages;

import p2p.Peer;

import java.io.Serializable;

public class UpdatePeersMessage implements Message, Serializable {
    private Peer next;
    private Peer previous;
    private Peer from;

    public UpdatePeersMessage(Peer next, Peer previous, Peer from) {
        this.next = next;
        this.previous = previous;
        this.from = from;
    }

    public Peer getNext() {
        return next;
    }

    public Peer getPrevious() {
        return previous;
    }

    @Override
    public Peer getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "UpdatePeersMessage{" +
                "next=" + next +
                ", previous=" + previous +
                '}';
    }
}
