package p2p.messages;

import p2p.Peer;

import java.io.Serializable;

public class UpdatePreviousPeerMessage implements Message, Serializable {
    private Peer newPrevPeer;

    public UpdatePreviousPeerMessage(Peer newPrevPeer) {
        this.newPrevPeer = newPrevPeer;
    }

    public Peer getNewPrevPeer() {
        return newPrevPeer;
    }

    @Override
    public String toString() {
        return "UpdatePreviousPeerMessage{" +
                "newPrevPeer=" + newPrevPeer +
                '}';
    }
}
