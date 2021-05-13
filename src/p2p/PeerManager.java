package p2p;

import p2p.messages.Message;

import java.util.ArrayList;

public final class PeerManager {
    private static ArrayList<Peer> peers = new ArrayList<>();

    public void addPeer(Peer peer) {
        this.peers.add(peer);
    }

    public static ArrayList<Peer> getPeers() {
        return peers;
    }

    public static void sendMessageTo(Message message, Peer from, Peer to) {
        for (Peer p : peers) {
            if (p.equals(to)) {
                p.receiveMessage(message, from);
                return;
            }
        }
    }

    public static void printPeerRing() {
        System.out.println("Peer2Peer ring:");

        SuperPeer superPeer = findSuperPeer();
        int counter = 0;
        System.out.print("[SuperPeer(" + superPeer.getId() + ")] -> ");

        Peer currentPeer = superPeer;
        while (currentPeer.getNext() != superPeer) {
            currentPeer = currentPeer.getNext();
            System.out.print("[Peer(" + currentPeer.getId() + ")] -> ");
        }

        currentPeer = currentPeer.getNext();
        System.out.println("[SuperPeer(" + currentPeer.getId() + ")]");
    }

    static public SuperPeer findSuperPeer() {
        for (Peer p : peers) {
            if (p instanceof SuperPeer) {
                return (SuperPeer) p;
            }
        }
        return null;
    }
}
