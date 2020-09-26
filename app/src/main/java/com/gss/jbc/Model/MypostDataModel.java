package com.gss.jbc.Model;

public class MypostDataModel {

    private String PostId, PostTitle, PostDetails, PostImage, FileType;

    public String getPostId() {
        return PostId;
    }
    public void setPostId(String id) {
        this.PostId = id;
    }

    public String getPostTitle() {
        return PostTitle;
    }
    public void setPostTitle(String title) {
        this.PostTitle = title;
    }

    public String getPostDetails() {
        return PostDetails;
    }
    public void setPostDetails(String message) {
        this.PostDetails = message;
    }

    public String getPostImage() {
        return PostImage;
    }
    public void setPostImage(String file_name) {
        this.PostImage = file_name;
    }

    public String getFileType() {
        return FileType;
    }
    public void setFileType(String file_type) {
        this.FileType = file_type;
    }
}
