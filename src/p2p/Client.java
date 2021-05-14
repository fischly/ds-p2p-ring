package p2p;

import p2p.messages.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        int peerId = Integer.parseInt(args[0]);
        String hostname = args[1];
        int port = Integer.parseInt(args[2]);
        boolean isSuperPeer = Boolean.parseBoolean(args[3]);

        boolean isRunning = true;
        byte[] recBuffer = new byte[1024 * 4];

        try {
            DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName(hostname));
            Peer thisPeer = isSuperPeer ?
                    new SuperPeer(peerId, socket.getLocalAddress(), port, socket) :
                    new Peer(peerId, socket.getLocalAddress(), port, socket);

            if (!isSuperPeer) {
                System.out.println("Trying to connect to super node...");
                thisPeer.connectToSuperNode(new Peer(0, InetAddress.getByName("localhost"), 13337, null));
            }

            InputThread inputThread = new InputThread(thisPeer);
            Thread thread = new Thread(inputThread);
            thread.start();

            while (isRunning && inputThread.isRunning()) {
                DatagramPacket recPacket = new DatagramPacket(recBuffer, recBuffer.length);
                socket.receive(recPacket);

                Message message = thisPeer.deserializeMessage(recPacket.getData(), recPacket.getOffset(), recPacket.getLength());
                System.out.println("[Received message] deserialized message: " + message + " from " + message.getFrom());
                thisPeer.receiveMessage(message, message.getFrom());
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static class InputThread implements Runnable {
        private Peer peer;
        private boolean isRunning;

        public InputThread(Peer peer) {
            this.peer = peer;

            this.isRunning = true;
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);

            while (isRunning) {
                System.out.println("Type in your operation [counting, disconnect, previous, next, info]:");
                String method = scanner.next();

                if (method.equalsIgnoreCase("counting")) {
                    System.out.println("Sending CountingMessage...");
                    try {
                        this.peer.sendCountingMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (method.equalsIgnoreCase("disconnect")) {
                    System.out.println("Sending disconnect message...");

                    this.isRunning = false;

                    try {
                        this.peer.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
                else if (method.equalsIgnoreCase("previous")) {
                    System.out.println("This peer's previous peer is: " + this.peer.getPrev());
                }
                else if (method.equalsIgnoreCase("next")) {
                    System.out.println("This peer's next peer is: " + this.peer.getNext());
                }
                else if (method.equalsIgnoreCase("this")) {
                    System.out.println("This peer = " + this.peer);
                }
                else if (method.equalsIgnoreCase("info")) {
                    System.out.println("this = " + peer + ", prev = " + peer.getPrev() + ", next = " + peer.getNext());
                }
            }
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        public boolean isRunning() {
            return isRunning;
        }
    }
}
