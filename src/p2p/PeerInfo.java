package p2p;

public class PeerInfo {
    private Peer previous;
    private Peer next;

    public PeerInfo(Peer previous, Peer next) {
        this.previous = previous;
        this.next = next;
    }

    public Peer getPrevious() {
        return previous;
    }

    public Peer getNext() {
        return next;
    }
}
