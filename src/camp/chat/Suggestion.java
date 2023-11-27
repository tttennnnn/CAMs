package camp.chat;

import camp.CampData;

/**
 * class of suggestion from camp committee
 * @author Anqi
 * @date 2023/11/22
 */
public class Suggestion extends Message implements CampData {
    /**
     * constructor of Suggestion
     * @param owner
     * @param content
     */
    Suggestion(String owner, String content) {
        super(owner, content);
    }

    /**
     * edit the content of suggestion
     * @param content
     */
    void editSuggestion(String content) {
        super.setContent(content);
    }
}
