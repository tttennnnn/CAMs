package camp.convo;

public class Message {
    private final String owner;
    private String content;
    Message(String owner, String content) {
        this.owner = owner;
        this.content = content;
    }
    public String getContent() { return content; }
    public String getOwner() { return owner; }
    public void setContent(String content) { this.content = content; }
}
