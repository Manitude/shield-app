package com.eighlark.shield.model;


/**
 * POJO representing a user's profile on Shield.
 */
public class User {

    public Long id;

    public String googleDisplayName = "";

    public String googlePublicProfileUrl = "";

    public String googlePublicProfilePhotoUrl = "";

    public String getProfileUrl() {
        return this.googlePublicProfilePhotoUrl.split("\\?sz=")[0] + "?sz=100";
    }

}
