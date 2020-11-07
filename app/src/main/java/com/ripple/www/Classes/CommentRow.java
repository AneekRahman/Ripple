package com.ripple.www.Classes;

public class CommentRow {

    String commentUserName, commentUserDpUrl, commentMainText, commentTimeStamp, commentRepliedToUserName;
    int commentUserID, commentRepliedToUserID;
    boolean commentReplied;

    public CommentRow(int commentUserID, String commentUserName, String commentUserDpUrl, String commentMainText, String commentTimeStamp, boolean commentReplied, int commentRepliedToUserID, String commentRepliedToUserName){

        this.commentUserID = commentUserID;
        this.commentUserName = commentUserName;
        this.commentUserDpUrl = commentUserDpUrl;
        this.commentMainText = commentMainText;
        this.commentTimeStamp = commentTimeStamp;
        this.commentReplied = commentReplied;
        this.commentRepliedToUserID = commentRepliedToUserID;
        this.commentRepliedToUserName = commentRepliedToUserName;

    }

    public int getCommentUserID() {
        return commentUserID;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public String getCommentUserDpUrl() {
        return commentUserDpUrl;
    }

    public String getCommentMainText() {
        return commentMainText;
    }

    public String getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public boolean getCommentReplied() {
        return commentReplied;
    }

    public int getCommentRepliedToUserID() {
        return commentRepliedToUserID;
    }

    public String getCommentRepliedToUserName() {
        return commentRepliedToUserName;
    }
}
