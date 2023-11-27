package camp.chat;

import camp.CampData;

/**
 * A class that represents request
 */
public class Enquiry implements CampData {
    /**
     * the message and answer user can see
     */
    private Message question, answer = null;

    /**
     * the constructor of the message question
     * @param question
     */
    Enquiry(Message question) {
        this.question = question;
    }

    /**
     * the constructor of the enquiry list
     * @param question
     * @param answer
     */
    Enquiry(Message question, Message answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * edit question to the enquiry list
     * @param content
     */
    void editQuestion(String content) { question.setContent(content); }

    /**
     * answer the question in the enquiry list
     * @param owner
     * @param content
     */
    void setAnswer(String owner, String content) { answer = new Message(owner, content); }

    /**
     * Get the question
     * @return the question
     */
    Message getQuestion() { return question; }

    /**
     * get the answer
     * @return the answer
     */
    Message getAnswer() { return answer; }

    /**
     * see if the question has been processed
     * @return boolean
     */
    boolean isProcessed() { return answer != null; }

}
