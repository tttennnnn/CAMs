package camp.chat;

import camp.CampData;
import camp.DataManager;

import java.util.ArrayList;

public interface ChatTypeManager extends DataManager {
    String[] getListAsStringArray();
    ArrayList<? extends CampData> unbindManager();
    ChatTypeManager getListManagerOfOwner(String userID);
}
