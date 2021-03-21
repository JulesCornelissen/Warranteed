package nl.tue.group2.Warranteed.chat;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {

    public String text;
    private long timestamp;
//    public String timestamp;
    private final Format timeFormatter = new SimpleDateFormat("HH:mm");
//    private String sender;

    public ChatMessage() {

    }

    public ChatMessage(String text) {
        this.timestamp = new Date().getTime();
//        this.timestamp = "dummydata";
        this.text = text;
    }

    /**
     * Set the text of this chat message, automatically updates the time.
     * @param text The text of the message.
     */
    public void setText(String text) {
        this.timestamp = new Date().getTime();
        this.text = text;
//        this.timestamp = "dummydata";
    }

    public String getText() {
        return this.text;
    }

    public String getTimestamp() {
        return this.timeFormatter.format(this.timestamp);
//        return this.timestamp;
    }

}
