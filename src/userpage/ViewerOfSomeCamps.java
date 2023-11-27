package userpage;

import camp.CampList;

/**
 * interface of some camp information viewer
 * @author Anqi
 * @date 2023/11/22
 */
public interface ViewerOfSomeCamps {
    /**
     * get List of Visible Camp
     * @return {@link CampList}
     */
    CampList getVisibleCampList();
}
