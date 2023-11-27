package camp.chat;

import camp.CampData;
import camp.DataManager;

import java.util.ArrayList;


/**
 * This interface represents a chat.
 */
public interface ChatTypeManager extends DataManager {
    /**
     * get the list as string array
     * @return the list as string array
     */
    String[] getListAsStringArray();

    /**
     * get the array list of unbind manager enquiries
     * @return the array list of unbind manager enquiries
     */
    ArrayList<? extends CampData> unbindManager();

    /**
     * get user's own list of enquiries
     * @param userID
     * @return user's own list of enquiries
     */
    ChatTypeManager getListManagerOfOwner(String userID);
}
