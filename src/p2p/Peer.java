package p2p;

import p2p.messages.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

public class Peer implements Serializable {
    protected int id;
    protected InetAddress ipAddress;
    protected int port;
    protected Peer prev;
    protected Peer next;
    protected transient DatagramSocket socket;

    public Peer(int id, InetAddress ipAddress, int port, DatagramSocket socket) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.socket = socket;
    }

    public void connectToSuperNode(Peer supernode) throws IOException {
        // connect to supernode und retrieve next and previous neighbour
//        PeerManager.sendMessageTo(new ConnectMessage(this), this, supernode);
        this.sendMessage(new ConnectMessage(this), supernode);
    }

    public void sendMessage(Message message, Peer to) throws IOException {
//        PeerManager.sendMessageTo(message, this, to);
        System.out.println("[sendMessage] trying to send message: " + message + " to " + to);
        // deserialize the message into a byte array to be sent over the network
        byte[] serializedMessage = this.serializeMessage(message);

        // create a UDP packet to send
        DatagramPacket packet = new DatagramPacket(serializedMessage, 0, serializedMessage.length, to.ipAddress, to.port);
        this.socket.send(packet);

        System.out.println("[sendMessage] message sent!");
    }

    public void receiveMessage(Message message, Peer from) throws IOException {
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

            System.out.println("[receiveMessage @ Peer" + this.id + "] received CountingMessage (" + countingMessage + ") from " + from);

            if (countingMessage.getIdList().contains(this.id)) {
                System.out.println("[CountingMessage @ Peer " + this.id + "] the CountingMessage contained this peers id -> done. Ring size = " + countingMessage.getIdList().size());
            } else {
                // add the peer's id to the counting list
                countingMessage.getIdList().add(this.id);
                countingMessage.setFrom(this);

                // determine to which neighbour to forward the message to
                Peer nextPeer = null;
                if (from.equals(this.next)) {
                    System.out.println("[CountingMessage] from was equal to this.next -> send CountingMessage to this.prev");
                    nextPeer = this.prev;
                } else {
                    System.out.println("[CountingMessage] from was equal not equal to this.next -> send CountingMessage to this.next");
                    nextPeer = this.next;
                }

                this.sendMessage(countingMessage, nextPeer);
            }
        }
    }

    public void sendCountingMessage() throws IOException {
        this.sendMessage(new CountingMessage(this.id, this), this.next);
    }

    public void disconnect() throws IOException {
        // send to next neighbouring peer to update it's previous peer to the previous neighbouring peer
        this.sendMessage(new UpdatePreviousPeerMessage(this.prev, this), this.next);

        // update next from the previous peer
        this.sendMessage(new UpdateNextPeerMessage(this.next, this), this.prev);
    }


    public int getId() {
        return id;
    }

    public InetAddress getIpAddress() {
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

    public Message deserializeMessage(byte[] buffer, int offset, int length) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer, offset, length));
        Message msg = (Message) objectInputStream.readObject();
        objectInputStream.close();

        return msg;
    }
}
