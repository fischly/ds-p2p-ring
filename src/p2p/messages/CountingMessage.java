package p2p.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CountingMessage implements Message, Serializable {
    private ArrayList<Integer> idList;

    public CountingMessage(int initialId) {
        this.idList = new ArrayList<>();
        this.idList.add(initialId);
    }

    public ArrayList<Integer> getIdList() {
        return idList;
    }

    @Override
    public String toString() {
        return "CountingMessage{" +
                "idList=" + Arrays.toString(idList.toArray()) +
                '}';
    }
}
