package camp.convo;

public class Suggestion {
    private Message suggestion;
    private boolean approved;
    public Suggestion(Message suggestion) {
        this.suggestion = suggestion;
    }
    public Suggestion(Message suggestion, boolean approved) {
        this.suggestion = suggestion;
        this.approved = approved;
    }

    public boolean isApproved() { return approved; }
    public Message getSuggestion() { return suggestion; }
}
