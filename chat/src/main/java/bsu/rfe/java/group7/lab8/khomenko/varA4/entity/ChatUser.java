package bsu.rfe.java.group7.lab8.khomenko.varA4.entity;

public class ChatUser {

    private String name; // Имя пользователя
    private long lastInteractionTime; // Последнее вр. взаим-вия с сервером (кол-во мкс с 01.01.1970)
    private String sessionId; // Идентификатор Java-сессии пользователя


    public ChatUser(String name,long lastInteractionTime,String sessionId) {
        super();
        this.name = name;
        this.lastInteractionTime = lastInteractionTime;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getLastInteractionTime() {
        return lastInteractionTime;
    }
    public void setLastInteractionTime(long lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;
    }

    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
