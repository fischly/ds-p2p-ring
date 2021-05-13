package p2p;

import p2p.messages.*;

import java.io.*;
import java.util.Objects;

public class Peer implements Serializable {
    protected int id;
    protected String ipAddress;
    protected int port;
    protected Peer prev;
    protected Peer next;

    public Peer(int id, String ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void connectToSuperNode(Peer supernode) {
        // connect to supernode und retrieve next and previous neighbour
        PeerManager.sendMessageTo(new ConnectMessage(), this, supernode);
    }

    public void sendMessage(Message message, Peer to) {
        PeerManager.sendMessageTo(message, this, to);
    }

    public void receiveMessage(Message message, Peer from) {
        if (message instanceof UpdateNextPeerMessage) {
            UpdateNextPeerMessage updateNextPeerMessage = (UpdateNextPeerMessage) message;
            System.out.println("[receiveMessage @ Peer" + this.id + "] received UpdateNextPeerMessage (" + updateNextPeerMessage.getNewNextPeer() + ")");

            this.next = updateNextPeerMessage.getNewNextPeer();
        }
        else if (message instanceof UpdatePreviousPeerMessage) {
            UpdatePreviousPeerMessage previousPeerMessage = (UpdatePreviousPeerMessage) message;
            System.out.println("[receiveMessage @ Peer" + this.id + "] received UpdatePreviousPeerMessage (" + previousPeerMessage.getNewPrevPeer() + ")");

            this.prev = previousPeerMessage.getNewPrevPeer();
        }
        else if (message instanceof UpdatePeersMessage) {
            UpdatePeersMessage updatePeersMessage = (UpdatePeersMessage) message;
            System.out.println("[receiveMessage @ Peer" + this.id + "] received UpdatePeersMessage (next = " + updatePeersMessage.getNext() + ", prev = " + updatePeersMessage.getPrevious() + ")");

            this.next = updatePeersMessage.getNext();
            this.prev = updatePeersMessage.getPrevious();
        }
        else if (message instanceof CountingMessage) {
            CountingMessage countingMessage = (CountingMessage) message;

            System.out.println("[receiveMessage @ Peer" + this.id + "] received CountingMessage (" + countingMessage + ")");

            if (countingMessage.getIdList().contains(this.id)) {
                System.out.println("[CountingMessage @ Peer " + this.id + "] the CountingMessage contained this peers id -> done. Ring size = " + countingMessage.getIdList().size());
            } else {
                // add the peer's id to the counting list
                countingMessage.getIdList().add(this.id);

                // determine to which neighbour to forward the message to
                Peer nextPeer = null;
                if (from.equals(this.next)) {
                    nextPeer = this.prev;
                } else {
                    nextPeer = this.next;
                }

                this.sendMessage(countingMessage, nextPeer);
            }
        }
    }

    public void sendCountingMessage() {
        this.sendMessage(new CountingMessage(this.id), this.next);
    }


    public int getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public Peer getPrev() {
        return prev;
    }

    public Peer getNext() {
        return next;
    }


    @Override
    public String toString() {
        return "Peer{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return id == peer.id &&
                port == peer.port &&
                Objects.equals(ipAddress, peer.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ipAddress, port);
    }

    private byte[] serializeMessage(Message message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(message);
        oos.close();

        return byteArrayOutputStream.toByteArray();
    }

    public Message deserializeMessage(byte[] buffer) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
        Message msg = (Message) objectInputStream.readObject();
        objectInputStream.close();

        return msg;
    }
}
