package com.ripple.www.Classes;

public class ProfileUserGridPost {

    String postUrl;
    int postType;

    public ProfileUserGridPost(int postType, String postUrl){

        this.postType = postType;
        this.postUrl = postUrl;

    }

    public int getPostType() {
        return postType;
    }

    public String getPostUrl() {
        return postUrl;
    }
}
