package camp.chat;

class Message {
    private final String owner;
    private String content;
    Message(String owner, String content) {
        this.owner = owner;
        this.content = content;
    }
    String getContent() { return content; }
    String getOwner() { return owner; }
    void setContent(String content) { this.content = content; }
}
