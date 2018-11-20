package org.ihci.itbs.model;

import org.ihci.itbs.model.pojo.User;

/**
 * @author Yukino Yukinoshita
 */

public class GlobalSettingModel {

    private static GlobalSettingModel INSTANCE = null;

    private String currentUserName;

    private GlobalSettingModel(){

    }

    public static GlobalSettingModel getInstance(){
        if(INSTANCE==null){
            INSTANCE = new GlobalSettingModel();
        }
        return INSTANCE;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }
}
