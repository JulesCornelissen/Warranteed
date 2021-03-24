package nl.tue.group2.Warranteed.chat;

import java.util.Date;

public class ChatMessage {

    private String text;
    private long timestamp;
    private String sender;

    public ChatMessage() {
    }

    public ChatMessage(String text, String sender) {
        this.timestamp = new Date().getTime();
        this.text = text;
        this.sender = sender;
    }

    /**
     * Set the text of this chat message, automatically updates the time.
     * @param text The text of the message.
     */
    public void setText(String text) {
        this.timestamp = new Date().getTime();
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }

    public long getTimestamp() {
        return this.timestamp;
    }



}
