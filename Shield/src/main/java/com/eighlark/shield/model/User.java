package com.eighlark.shield.model;


/**
 * POJO representing a user's profile on Shield.
 */
public class User {

    public String id;
    public String googleDisplayName = "";
    public String googlePublicProfileUrl = "";
    public String googlePublicProfilePhotoUrl = "";

    public User(
            String id,
            String googleDisplayName,
            String googlePublicProfileUrl,
            String googlePublicProfilePhotoUrl) {
        this.id = id;
        this.googleDisplayName = googleDisplayName;
        this.googlePublicProfileUrl = googlePublicProfileUrl;
        this.googlePublicProfilePhotoUrl = googlePublicProfilePhotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleDisplayName() {
        return googleDisplayName;
    }

    public void setGoogleDisplayName(String googleDisplayName) {
        this.googleDisplayName = googleDisplayName;
    }

    public String getGooglePublicProfileUrl() {
        return googlePublicProfileUrl;
    }

    public void setGooglePublicProfileUrl(String googlePublicProfileUrl) {
        this.googlePublicProfileUrl = googlePublicProfileUrl;
    }

    public String getGooglePublicProfilePhotoUrl() {
        return googlePublicProfilePhotoUrl;
    }

    public void setGooglePublicProfilePhotoUrl(String googlePublicProfilePhotoUrl) {
        this.googlePublicProfilePhotoUrl = googlePublicProfilePhotoUrl;
    }

    public String getProfileUrl() {
        return this.googlePublicProfilePhotoUrl.split("\\?sz=")[0] + "?sz=100";
    }

}
