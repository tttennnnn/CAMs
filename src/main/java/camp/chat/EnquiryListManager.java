package camp.chat;

import app.CAMsApp;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class EnquiryListManager implements ChatTypeManager {
    private ArrayList<Enquiry> enquiries;
    private EnquiryListManager(ArrayList<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }
    public int size() { return enquiries.size(); }
    public String getQuestionOwner(int index) { return enquiries.get(index).getQuestion().getOwner(); }
    public String getQuestionContent(int index) { return enquiries.get(index).getQuestion().getContent(); }
    public String getAnswerOwner(int index) { return enquiries.get(index).getAnswer().getOwner(); }
    public String getAnswerContent(int index) { return enquiries.get(index).getAnswer().getContent(); }
    public boolean isProcessed(int index) { return enquiries.get(index).isProcessed(); }
    public void editQuestion(int index, String content) { enquiries.get(index).editQuestion(content); }
    public void setAnswer(int index, String owner, String content) { enquiries.get(index).setAnswer(owner, content); }
    public void addEnquiry(String owner, String content) { enquiries.add(new Enquiry(new Message(owner, content))); }
    public void removeEnquiry(int index) { enquiries.remove(index); }
    @Override
    public ArrayList<Enquiry> unbindManager() { return enquiries; }
    @Override
    public EnquiryListManager getListManagerOfOwner(String userID) {
        ArrayList<Enquiry> res = new ArrayList<>();
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getQuestion().getOwner().equals(userID))
                res.add(enquiry);
        }
        return EnquiryListManager.createInstance(res);
    }
    @Override
    public String[] getListAsStringArray() {
        String[] res = new String[2 * enquiries.size()];
        for (int i = 0; i < size(); i++) {
            res[2*i] = getQuestionOwner(i) + "=" + getQuestionContent(i);
            res[2*i+1] = (isProcessed(i)) ?
                getAnswerOwner(i) + "=" + getAnswerContent(i) : "";
        }
        return res;
    }
    @Override
    public void updateToFile(String campName) {
        List<String[]> enquiryLines = AppUtil.getDataFromCSV(CAMsApp.getCampEnquiryFile());
        String[] newEnquiryLine = ArrayUtils.addAll(
            new String[]{campName}, getListAsStringArray()
        );
        int row;
        for (row = 1; row < enquiryLines.size(); row++) {
            String[] enquiryLine = enquiryLines.get(row);
            if (enquiryLine[0].equals(campName)) {
                enquiryLines.set(row, newEnquiryLine);
                break;
            }
        }
        if (row >= enquiryLines.size()) {
            enquiryLines.add(newEnquiryLine);
        }
        AppUtil.overwriteCSV(CAMsApp.getCampEnquiryFile(), enquiryLines);
    }

    // static methods
    public static EnquiryListManager createInstance(ArrayList<Enquiry> enquiries) {
        return new EnquiryListManager(enquiries);
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
}
