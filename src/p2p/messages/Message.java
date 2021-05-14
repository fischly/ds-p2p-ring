package p2p.messages;


import p2p.Peer;

import java.io.Serializable;

public interface Message extends Serializable {
    Peer getFrom();
}
