package camp.convo;

import app.CAMsApp;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.ArrayUtils;
import util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Suggestion extends Message {
    public Suggestion(String owner, String content) {
        super(owner, content);
    }

    public static String[] getSuggestionListAsStringArray(ArrayList<Suggestion> suggestions) {
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

    public static void updateSuggestionsToFile(String campName, ArrayList<Suggestion> suggestions) throws IOException, CsvException {
        CSVReader campSuggestionReader = AppUtil.getCSVReader(CAMsApp.getCampSuggestionFile());
        List<String[]> suggestionLines = campSuggestionReader.readAll();
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
