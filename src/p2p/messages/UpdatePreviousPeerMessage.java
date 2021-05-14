package p2p.messages;

import p2p.Peer;

import java.io.Serializable;

public class UpdatePreviousPeerMessage implements Message, Serializable {
    private Peer newPrevPeer;
    private Peer from;

    public UpdatePreviousPeerMessage(Peer newPrevPeer, Peer from) {
        this.newPrevPeer = newPrevPeer;
        this.from = from;
    }

    public Peer getNewPrevPeer() {
        return newPrevPeer;
    }

    @Override
    public Peer getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "UpdatePreviousPeerMessage{" +
                "newPrevPeer=" + newPrevPeer +
                '}';
    }
}
