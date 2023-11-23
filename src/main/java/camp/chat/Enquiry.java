package camp.chat;

import camp.CampData;

public class Enquiry implements CampData {
    private Message question, answer = null;
    Enquiry(Message question) {
        this.question = question;
    }
    Enquiry(Message question, Message answer) {
        this.question = question;
        this.answer = answer;
    }
    void editQuestion(String content) { question.setContent(content); }
    void setAnswer(String owner, String content) { answer = new Message(owner, content); }
    Message getQuestion() { return question; }
    Message getAnswer() { return answer; }
    boolean isProcessed() { return answer != null; }

}
