package camp.chat;

/**
 * class that represent message
 * @author Anqi
 * @date 2023/11/22
 */
class Message {
    /**
     * enquiry message owner
     */
    private final String owner;
    /**
     *content of message
     */
    private String content;

    /**
     * constructor of message
     * @param owner
     * @param content
     */
    Message(String owner, String content) {
        this.owner = owner;
        this.content = content;
    }

    /**
     * get the content of message
     * @return content of message
     */
    String getContent() { return content; }

    /**
     * get the owner of message
     * @return owner of message
     */
    String getOwner() { return owner; }

    /**
     * set the content of message
     * @param content
     */
    void setContent(String content) { this.content = content; }
}
