package camp.chat;

import app.CAMsApp;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an Enquiry List Manager.
 */
public class EnquiryListManager implements ChatTypeManager {
    /**
     * list of enquiries
     */
    private ArrayList<Enquiry> enquiries;

    /**
     * constructor of Enquiry List Manager
     * @param enquiries
     */
    private EnquiryListManager(ArrayList<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }

    /**
     * get size of enquiry
     * @return size of enquiry
     */
    public int size() { return enquiries.size(); }

    /**
     * get the question and id of question owner
     * @param index
     * @return question and id of question owner
     */
    public String getQuestionOwner(int index) { return enquiries.get(index).getQuestion().getOwner(); }

    /**
     * get the question and content of question
     * @param index
     * @return question and content of question
     */
    public String getQuestionContent(int index) { return enquiries.get(index).getQuestion().getContent(); }

    /**
     * get the answer and id of answer owner
     * @param index
     * @return answer and id of answer owner
     */
    public String getAnswerOwner(int index) { return enquiries.get(index).getAnswer().getOwner(); }

    /**
     * get the answer and content of answer
     * @param index
     * @return answer and content of answer
     */
    public String getAnswerContent(int index) { return enquiries.get(index).getAnswer().getContent(); }

    /**
     * see if enquiry has been processed
     * @param index
     * @return boolean
     */
    public boolean isProcessed(int index) { return enquiries.get(index).isProcessed(); }

    /**
     * edit the question user have made
     * @param index
     * @param content
     */
    public void editQuestion(int index, String content) { enquiries.get(index).editQuestion(content); }

    /**
     * set answer to the enquiry
     * @param index
     * @param owner
     * @param content
     */
    public void setAnswer(int index, String owner, String content) { enquiries.get(index).setAnswer(owner, content); }

    /**
     * add enquiry to system
     * @param owner
     * @param content
     */
    public void addEnquiry(String owner, String content) { enquiries.add(new Enquiry(new Message(owner, content))); }

    /**
     * delete the enquiry
     * @param index
     */
    public void removeEnquiry(int index) { enquiries.remove(index); }

    /**
     * get the array list of unbind manager enquiries
     * @return array list of unbind manager enquiries
     */
    @Override
    public ArrayList<Enquiry> unbindManager() { return enquiries; }

    /**
     * get user's own list of enquiries
     * @param userID
     * @return user's own list of enquiries
     */
    @Override
    public EnquiryListManager getListManagerOfOwner(String userID) {
        ArrayList<Enquiry> res = new ArrayList<>();
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getQuestion().getOwner().equals(userID))
                res.add(enquiry);
        }
        return EnquiryListManager.createInstance(res);
    }

    /**
     * get complete list of enquiry
     * @return complete list of enquiry
     */
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

    /**
     * update the enquiry information to the data file
     * @param campName
     */
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

    /**
     * create instance in enquiry list
     * @param enquiries
     * @return new EnquiryListManager
     */// static methods
    public static EnquiryListManager createInstance(ArrayList<Enquiry> enquiries) {
        return new EnquiryListManager(enquiries);
    }

    /**
     * get enquiry list as a form of array list
     * @param enquiries
     * @return enquiry list as a form of array list
     */
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
