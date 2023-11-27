package camp.chat;

import app.CAMsApp;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * class of Suggestion List Manager
 * @author Anqi
 * @date 2023/11/22
 */
public class SuggestionListManager implements ChatTypeManager {
    /**
     * suggestions from users
     */
    private ArrayList<Suggestion> suggestions;

    /**
     * constructor of Suggestion List Manager
     * @param suggestions
     */
    private SuggestionListManager(ArrayList<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * size of suggestion
     * @return size of suggestion
     */
    public int size() { return suggestions.size(); }

    /**
     * get the owner of suggestion
     * @param index
     * @return owner of suggestion
     */
    public String getOwner(int index) { return suggestions.get(index).getOwner(); }

    /**
     * get the content of suggestion
     * @param index
     * @return content of suggestion
     */
    public String getContent(int index) { return suggestions.get(index).getContent(); }

    /**
     * edit the content of suggestion
     * @param index
     * @param content
     */
    public void editSuggestion(int index, String content) { suggestions.get(index).editSuggestion(content); }

    /**
     * add suggestion to the system
     * @param owner
     * @param content
     */
    public void addSuggestion(String owner, String content) { suggestions.add(new Suggestion(owner, content)); }

    /**
     * delete the suggestion user had made
     * @param index
     */
    public void removeSuggestion(int index) { suggestions.remove(index); }

    /**
     * get the array list of unbind manager suggestions
     * @return array list of unbind manager suggestions
     */
    @Override
    public ArrayList<Suggestion> unbindManager() { return suggestions; }

    /**
     * get user's own list of suggestions
     * @param userID
     * @return user's own list of suggestions
     */
    @Override
    public SuggestionListManager getListManagerOfOwner(String userID) {
        ArrayList<Suggestion> res = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            if (suggestion.getOwner().equals(userID))
                res.add(suggestion);
        }
        return SuggestionListManager.createInstance(res);
    }

    /**
     * get complete list of suggestions
     * @return complete list of suggestions
     */
    @Override
    public String[] getListAsStringArray() {
        String[] res = new String[suggestions.size()];
        for (int i = 0; i < suggestions.size(); i++) {
            res[i] = getOwner(i) + "=" + getContent(i);
        }
        return res;
    }

    /**
     * update the suggestion information to the data file
     * @param campName
     */
    @Override
    public void updateToFile(String campName) {
        List<String[]> suggestionLines = AppUtil.getDataFromCSV(CAMsApp.getCampSuggestionFile());
        String[] newSuggestionLine = ArrayUtils.addAll(
            new String[]{campName}, getListAsStringArray()
        );
        int row;
        for (row = 1; row < suggestionLines.size(); row++) {
            String[] suggestionLine = suggestionLines.get(row);
            if (suggestionLine[0].equals(campName)) {
                suggestionLines.set(row, newSuggestionLine);
                break;
            }
        }
        if (row >= suggestionLines.size()) {
            suggestionLines.add(newSuggestionLine);
        }
        AppUtil.overwriteCSV(CAMsApp.getCampSuggestionFile(), suggestionLines);
    }

    /**
     * create instance in suggestion list
     * @param suggestions
     * @return new Suggestion List
     */// static methods
    public static SuggestionListManager createInstance(ArrayList<Suggestion> suggestions) {
        return new SuggestionListManager(suggestions);
    }

    /**
     * get suggestion list as a form of array list
     * @param suggestions
     * @return suggestion list as a form of array list
     */
    public static ArrayList<Suggestion> getSuggestionListAsArrayList(String[] suggestions) {
        if (suggestions.length == 0)
            return new ArrayList<>();

        ArrayList<Suggestion> res = new ArrayList<>();
        for (String pair : suggestions) {
            String owner = pair.split("=")[0];
            String content = pair.split("=")[1];
            res.add(new Suggestion(owner, content));
        }
        return res;
    }
}
