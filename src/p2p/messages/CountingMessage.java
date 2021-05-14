package p2p.messages;

import p2p.Peer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CountingMessage implements Message, Serializable {
    private ArrayList<Integer> idList;
    private Peer from;

    public CountingMessage(int initialId, Peer from) {
        this.idList = new ArrayList<>();
        this.idList.add(initialId);
        this.from = from;
    }

    public ArrayList<Integer> getIdList() {
        return idList;
    }

    @Override
    public Peer getFrom() {
        return from;
    }

    public void setFrom(Peer from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "CountingMessage{" +
                "idList=" + Arrays.toString(idList.toArray()) +
                '}';
    }
}
