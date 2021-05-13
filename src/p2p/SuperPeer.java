package p2p;

import p2p.messages.ConnectMessage;
import p2p.messages.Message;
import p2p.messages.UpdateNextPeerMessage;
import p2p.messages.UpdatePeersMessage;

public class SuperPeer extends Peer {

    public SuperPeer(int id, String ipAddress, int port) {
        super(id, ipAddress, port);
    }

    /**
     * Handle a newly connected java.Peer and returns the new neighbours this new java.Peer should connect to.
     * @return the previous and next peer that should be connected to.
     */
    public void handleNewConnection(Peer newPeer) {
        // handle the case, that there is no peer connected
        if (this.next == null && this.prev == null) {
            this.next = this.prev = newPeer;

            this.sendMessage(new UpdatePeersMessage(this, this), newPeer);
            return;
        }

        // so we have already at least one connected client, so try to insert the new node between the last added node and the supernode
        // first we tell the previous node of the super node (this) to update it's next node from the supernode to the newly added node
        this.sendMessage(new UpdateNextPeerMessage(newPeer), this.prev);
        Peer oldPrev = this.prev;

        // then we update the supernodes previous
        this.prev = newPeer;

        // finally sent the new peer his neighbours
        this.sendMessage(new UpdatePeersMessage(this, oldPrev), newPeer);
    }

    @Override
    public void receiveMessage(Message message, Peer from) {
        super.receiveMessage(message, from);

        if (message instanceof ConnectMessage) {
            ConnectMessage connectMessage = (ConnectMessage) message;
            System.out.println("[receivedMessage] received ConnectMessage from " + from);

            // TODO
            this.handleNewConnection(from);
        }
    }


    @Override
    public String toString() {
        return "SuperPeer{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }
}
