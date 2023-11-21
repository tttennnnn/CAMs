package camp.convo;

import app.CAMsApp;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class Enquiry {
    private Message question, answer;
    private Enquiry(Message question, Message answer) {
        this.question = question;
        this.answer = answer;
    }
    public boolean isProcessed() { return answer != null; }
    public Message getQuestion() { return question; }
    public Message getAnswer() { return answer; }
    public void editQuestion(String content) { question.setContent(content); }
    public void setAnswer(String owner, String content) { this.answer = new Message(owner, content); }
    public static Enquiry createEnquiry(String questionOwner, String questionContent) {
        return new Enquiry(new Message(questionOwner, questionContent), null);
    }

    private static String[] getEnquiryListAsStringArray(ArrayList<Enquiry> enquiries) {
        String[] res = new String[2 * enquiries.size()];
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry enquiry = enquiries.get(i);
            res[2*i] = enquiry.getQuestion().getOwner() + "=" + enquiry.getQuestion().getContent();
            res[2*i+1] = (enquiry.isProcessed()) ?
                enquiry.getAnswer().getOwner() + "=" + enquiry.getAnswer().getContent() : "";
        }
        return res;
    }
    public static ArrayList<Enquiry> getEnquiryListAsArrayList(String[] enquiries) {
        ArrayList<Enquiry> res = new ArrayList<>();
        for (int i = 0; i < enquiries.length; i += 2) {
            String questionStr = enquiries[i];
            String answerStr = enquiries[i+1];

            Message question = new Message(questionStr.split("=")[0], questionStr.split("=")[1]);
            Message answer = (answerStr.isEmpty()) ?
                null : new Message(answerStr.split("=")[0], answerStr.split("=")[1]);
            res.add(new Enquiry(question, answer));
        }
        return res;
    }

    public static void updateEnquiriesToFile(String campName, ArrayList<Enquiry> enquiries) {
        List<String[]> enquiryLines = AppUtil.getDataFromCSV(CAMsApp.getCampEnquiryFile());
        for (int row = 1; row < enquiryLines.size(); row++) {
            String[] enquiryLine = enquiryLines.get(row);
            if (enquiryLine[0].equals(campName)) {
                String[] newEnquiries = ArrayUtils.addAll(
                    new String[]{enquiryLine[0]}, Enquiry.getEnquiryListAsStringArray(enquiries)
                );
                enquiryLines.set(row, newEnquiries);
            }
        }
        AppUtil.overwriteCSV(CAMsApp.getCampEnquiryFile(), enquiryLines);
    }
}
