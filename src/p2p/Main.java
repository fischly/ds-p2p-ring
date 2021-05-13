package p2p;

import p2p.messages.CountingMessage;

public class Main {
    public static void main(String[] args) {
        SuperPeer superPeer = new SuperPeer(0, "1.1.1.1", 80);
        PeerManager.getPeers().add(superPeer);

        System.out.println(superPeer);

        Peer p1 = new Peer(1, "1.1.1.2", 81);
        PeerManager.getPeers().add(p1);
        p1.connectToSuperNode(superPeer);

        Peer p2 = new Peer(2, "1.1.1.2", 82);
        PeerManager.getPeers().add(p2);
        p2.connectToSuperNode(superPeer);

//        Peer p3 = new Peer(3, "1.1.1.3", 83);
//        PeerManager.getPeers().add(p3);
//        p3.connectToSuperNode(superPeer);


        PeerManager.printPeerRing();

        System.out.println("SuperPeer neighbours: next = " + superPeer.next + ", prev = " + superPeer.prev);
        System.out.println("Peer 1    neighbours: next = " + p1.next + ", prev = " + p1.prev);
        System.out.println("Peer 2    neighbours: next = " + p2.next + ", prev = " + p2.prev);
//        System.out.println("Peer 3    neighbours: next = " + p3.next + ", prev = " + p3.prev);

        System.out.println("\np1 sending CountingMessage:");
        superPeer.sendCountingMessage();
    }
}
