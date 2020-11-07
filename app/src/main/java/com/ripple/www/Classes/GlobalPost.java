package com.ripple.www.Classes;

public class GlobalPost {

    int postID, postHeartCount, postViewsCount;

    public GlobalPost(int postID, int postHeartCount, int postViewsCount){

        this.postID = postID;
        this.postHeartCount = postHeartCount;
        this.postViewsCount = postViewsCount;

    }

    public int getPostID() {
        return postID;
    }

    public int getPostHeartCount() {
        return postHeartCount;
    }

    public int getPostViewsCount() {
        return postViewsCount;
    }
}
