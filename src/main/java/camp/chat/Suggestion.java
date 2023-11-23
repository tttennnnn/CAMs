package camp.chat;

import camp.CampData;

public class Suggestion extends Message implements CampData {
    Suggestion(String owner, String content) {
        super(owner, content);
    }
    void editSuggestion(String content) {
        super.setContent(content);
    }
}
