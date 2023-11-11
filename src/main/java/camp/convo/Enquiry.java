package camp.convo;

public class Enquiry {
    private Message question, answer = null;
    public Enquiry(Message question) {
        this.question = question;
    }
    public Enquiry(Message question, Message answer) {
        this.question = question;
        this.answer = answer;
    }
    public void setAnswer(Message answer) { this.answer = answer; }
    public boolean isProcessed() { return answer != null; }
    public Message getQuestion() { return question; }
    public Message getAnswer() { return answer; }
}
