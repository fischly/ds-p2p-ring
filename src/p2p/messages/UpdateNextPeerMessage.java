package p2p.messages;

import p2p.Peer;

import java.io.*;

public class UpdateNextPeerMessage implements Message, Serializable {
    private Peer newNextPeer;

    public UpdateNextPeerMessage(Peer newNextPeer) {
        this.newNextPeer = newNextPeer;
    }

    public Peer getNewNextPeer() {
        return newNextPeer;
    }

    @Override
    public String toString() {
        return "UpdateNextPeerMessage{" +
                "newNextPeer=" + newNextPeer +
                '}';
    }
}
