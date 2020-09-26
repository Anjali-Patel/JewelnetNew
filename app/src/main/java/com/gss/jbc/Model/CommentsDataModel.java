package com.gss.jbc.Model;

public class CommentsDataModel {

    private String Comment_id;
    private String Commentor_id;
    private String News_id;
    private String Comment;
    private String Title;
    private String News_description;
    private String Fname;
    private String Lname;
    private String News_file_name;

    public String getComment_id() {
        return Comment_id;
    }
    public void setComment_id(String comment_id) {
        this.Comment_id = comment_id;
    }

    public String getCommentor_id() {
        return Commentor_id;
    }
    public void setCommentor_id(String commentor_id) {
        this.Commentor_id = commentor_id;
    }

    public String getNews_id() {
        return News_id;
    }
    public void setNews_id(String news_id) {
        this.News_id = news_id;
    }

    public String getComment() { return Comment; }
    public void setComment(String comment) {
        this.Comment = comment;
    }

    public String getTitle() { return Title; }
    public void setTitle(String title) {
        this.Title = title;
    }

    public String getNews_description() { return News_description; }
    public void setNews_description(String news_description) { this.News_description = news_description; }

    public String getFname() { return Fname; }
    public void setFname(String fname) {
        this.Fname = fname;
    }

    public String getLname() { return Lname; }
    public void setLname(String lname) {
        this.Lname = lname;
    }

    public String getNews_file_name() { return News_file_name; }
    public void setNews_file_name(String news_file_name) { this.News_file_name = news_file_name; }

}
