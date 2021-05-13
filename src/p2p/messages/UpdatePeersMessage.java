package p2p.messages;

import p2p.Peer;

import java.io.Serializable;

public class UpdatePeersMessage implements Message, Serializable {
    private Peer next;
    private Peer previous;

    public UpdatePeersMessage(Peer next, Peer previous) {
        this.next = next;
        this.previous = previous;
    }

    public Peer getNext() {
        return next;
    }

    public Peer getPrevious() {
        return previous;
    }

    @Override
    public String toString() {
        return "UpdatePeersMessage{" +
                "next=" + next +
                ", previous=" + previous +
                '}';
    }
}
