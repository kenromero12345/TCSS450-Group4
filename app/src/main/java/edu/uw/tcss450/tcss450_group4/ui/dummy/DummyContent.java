package edu.uw.tcss450.tcss450_group4.ui.dummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent implements Serializable {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<connection> CONNECTIONS = new ArrayList<connection>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, connection> CONNECTION_MAP = new HashMap<String, connection>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createConnection(i));
        }
    }

    private static void addItem(connection item) {
        CONNECTIONS.add(item);
        CONNECTION_MAP.put(item.id, item);
    }

    private static connection createConnection(int position) {
        return new connection(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class connection {
        public final String id;
        public final String name;
        public final String details;

        public connection(String id, String name, String details) {
            this.id = id;
            this.name = name;
            this.details = details;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
