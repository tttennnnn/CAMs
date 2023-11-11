package camp.convo;

public class Message {
    private final String owner;
    private String content;

    public Message(String owner, String content) {
        this.owner = owner;
        this.content = content;
    }
    public String getOwner() { return owner; }
    public String getContent() { return content; }
    public void editContent(String content) { this.content = content; }
}
