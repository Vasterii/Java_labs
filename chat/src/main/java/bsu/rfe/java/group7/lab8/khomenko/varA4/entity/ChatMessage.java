package bsu.rfe.java.group7.lab8.khomenko.varA4.entity;

public class ChatMessage {

    private String message; // Текст сообщения
    private ChatUser author; // Автор сообщения
    private long timestamp; // Временная метка сообщения (в микросекундах)


    public ChatMessage(String message, ChatUser author, long timestamp) {
        super();
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public ChatUser getAuthor() {
        return author;
    }
    public void setAuthor(ChatUser author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
