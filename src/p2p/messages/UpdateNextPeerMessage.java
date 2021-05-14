package p2p.messages;


import p2p.Peer;

import java.io.*;

public class UpdateNextPeerMessage implements Message, Serializable {
    private Peer newNextPeer;
    private Peer from;

    public UpdateNextPeerMessage(Peer newNextPeer, Peer from) {
        this.newNextPeer = newNextPeer;
        this.from = from;
    }

    public Peer getNewNextPeer() {
        return newNextPeer;
    }

    @Override
    public Peer getFrom() {
        return from;
    }
}
