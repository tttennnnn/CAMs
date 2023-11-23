package camp.chat;

import app.CAMsApp;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class SuggestionListManager implements ChatTypeManager {
    private ArrayList<Suggestion> suggestions;
    private SuggestionListManager(ArrayList<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
    public int size() { return suggestions.size(); }
    public String getOwner(int index) { return suggestions.get(index).getOwner(); }
    public String getContent(int index) { return suggestions.get(index).getContent(); }
    public void editSuggestion(int index, String content) { suggestions.get(index).editSuggestion(content); }
    public void addSuggestion(String owner, String content) { suggestions.add(new Suggestion(owner, content)); }
    public void removeSuggestion(int index) { suggestions.remove(index); }
    @Override
    public ArrayList<Suggestion> unbindManager() { return suggestions; }
    @Override
    public SuggestionListManager getListManagerOfOwner(String userID) {
        ArrayList<Suggestion> res = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            if (suggestion.getOwner().equals(userID))
                res.add(suggestion);
        }
        return SuggestionListManager.createInstance(res);
    }
    @Override
    public String[] getListAsStringArray() {
        String[] res = new String[suggestions.size()];
        for (int i = 0; i < suggestions.size(); i++) {
            res[i] = getOwner(i) + "=" + getContent(i);
        }
        return res;
    }
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

    // static methods
    public static SuggestionListManager createInstance(ArrayList<Suggestion> suggestions) {
        return new SuggestionListManager(suggestions);
    }
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
