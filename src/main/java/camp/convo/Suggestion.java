package camp.convo;

import app.CAMsApp;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class Suggestion extends Message {
    private Suggestion(String owner, String content) {
        super(owner, content);
    }
    public void editSuggestion(String content) { setContent(content); }
    public static Suggestion createSuggestion(String owner, String content) {
        return new Suggestion(owner, content);
    }

    private static String[] getSuggestionListAsStringArray(ArrayList<Suggestion> suggestions) {
        String[] res = new String[suggestions.size()];
        for (int i = 0; i < suggestions.size(); i++) {
            Suggestion suggestion = suggestions.get(i);
            res[i] = suggestion.getOwner() + "=" + suggestion.getContent();
        }
        return res;
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

    public static void updateSuggestionsToFile(String campName, ArrayList<Suggestion> suggestions) {
        List<String[]> suggestionLines = AppUtil.getDataFromCSV(CAMsApp.getCampSuggestionFile());
        for (int row = 1; row < suggestionLines.size(); row++) {
            String[] suggestionLine = suggestionLines.get(row);
            if (suggestionLine[0].equals(campName)) {
                String[] newSuggestions = ArrayUtils.addAll(
                    new String[]{suggestionLine[0]}, Suggestion.getSuggestionListAsStringArray(suggestions)
                );
                suggestionLines.set(row, newSuggestions);
                break;
            }
        }
        AppUtil.overwriteCSV(CAMsApp.getCampSuggestionFile(), suggestionLines);
    }
}
