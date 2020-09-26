package com.gss.jbc;

import org.json.JSONObject;

public class NewsItemStructure {
    private String nCompanyAddress="";
    private  String nLocation="";
    private String nStatus="";
    private String nDesignation="";
    private String nFileName="";
    private  String nFileType="";
    private String LargeImage="";
    private String nMessage="";
    private String nId="";
    private  String nUserId="";
    private String nBusinessNature="";
    private String nCompanyName="";
    private String nTitle="";
    private String nDOB="";
    private  String nFname="";
    private String nLname="";
    private  String nProfilePic="";
    private  boolean nIsAdvertise = false;
    private  boolean nHasProfilePic = false;
    private  String nAdvertiseImage = "";
    private String nAdvLink = "";
    private String AdvertisementImageName = "";
    private String Advertisementbasepath = "";
    private String ViewCount="";
    private String LikeCount="";
    private String CommentCount="";
    private String IsLiked="";
    private String TotalShares = "";





    public String getAdvertisementImageName() {
        return AdvertisementImageName;
    }

    public void setAdvertisementImageName(String advertisementImageName) {
        AdvertisementImageName = advertisementImageName;
    }

    public String getnAdvLink() {
        return nAdvLink;
    }

    public void setnAdvLink(String nAdvLink) {
        this.nAdvLink = nAdvLink;
    }

    public String getLargeImage() {
        return LargeImage;
    }

    public void setLargeImage(String largeImage) {
        LargeImage = largeImage;
    }

    public String getAdvertisementbasepath() {
        return Advertisementbasepath;
    }

    public void setAdvertisementbasepath(String advertisementbasepath) {
        Advertisementbasepath = advertisementbasepath;
    }

    NewsItemStructure(JSONObject jsonNews, String news_base_path, String profile_base_path, String advertisement_base_path, boolean isAdvertise, String newsviewcount){
        try{
            if(jsonNews.has("title"))
                setnTitle(jsonNews.getString("title"));
            if(jsonNews.has("message"))
                setnMessage(jsonNews.getString("message"));


            if(jsonNews.has("file_name")){
                String file_path = news_base_path + "/" +jsonNews.getString("file_name");
                setnFileName(file_path);
            }

            if (jsonNews.has("large_image")){
                String LargeImage = news_base_path + "/" +jsonNews.getString("large_image");
                setLargeImage(LargeImage);
            }

            else if(jsonNews.has("image")) {
                String file_path = news_base_path + "/" + jsonNews.getString("image");
                setnFileName(file_path);
            }
            if(jsonNews.has("file_type"))
                setnFileType(jsonNews.getString("file_type"));

            if(jsonNews.has("user_id") && jsonNews.has("profile_picture")){

                if(jsonNews.getString("profile_picture").length() > 0)
                {
                    setnHasProfilePic(true);
                    setnProfilePic(profile_base_path + "/" +jsonNews.getString("user_id")+"/"+ jsonNews.getString("profile_picture"));
                }
            }

            if(jsonNews.has("company_name"))
                setnCompanyName(jsonNews.getString("company_name"));
            if(jsonNews.has("fname"))
                setnFname(jsonNews.getString("fname"));
            if(jsonNews.has("lname"))

                setnLname(jsonNews.getString("lname"));
            if(jsonNews.has("designation"))
                setnDesignation(jsonNews.getString("designation"));
            if(jsonNews.has("location"))
                setnCompanyAddress(jsonNews.getString("location"));
            if(jsonNews.has("id"))
                setnId(jsonNews.getString("id"));
            if(jsonNews.has("user_id"))
                setnUserId(jsonNews.getString("user_id"));



            setnIsAdvertise(isAdvertise);

            if(isAdvertise && jsonNews.has("image")){

                if(jsonNews.getString("image").length() > 0)
                {
                    setnAdvertiseImage(advertisement_base_path + "/" +jsonNews.getString("image"));
                }
                if(jsonNews.getString("image").length() > 0)
                {
                    setAdvertisementImageName(jsonNews.getString("image"));
                }
            }
            if (isAdvertise && jsonNews.has("link")){
                setnAdvLink(jsonNews.getString("link"));
            }

            setAdvertisementbasepath(advertisement_base_path);

            //setCount(newsviewcount);


            if(jsonNews.has("total_views"))
                setCount(jsonNews.getString("total_views"));

            if(jsonNews.has("total_likes"))
                setLikeCount(jsonNews.getString("total_likes"));

            if(jsonNews.has("total_comments"))
                setCommentCount(jsonNews.getString("total_comments"));

            if(jsonNews.has("is_liked"))
                setIsLiked(jsonNews.getString("is_liked"));

            if (jsonNews.has("total_shares"))
                setTotalShares(jsonNews.getString("total_shares"));



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void setTotalShares(String total_shares) {
        this.TotalShares = total_shares;
    }
    public String getTotalShares() {
        return TotalShares;
    }

    private void setCount(String views_count) {
        this.ViewCount = views_count;
    }
    public String getCount() {
        return ViewCount;
    }

    private void setLikeCount(String total_likes) {
        this.LikeCount = total_likes;
    }
    public String getLikeCount() { return LikeCount; }

    private void setCommentCount(String total_comments) {
        this.CommentCount = total_comments;
    }
    public String getCommentCount() { return CommentCount; }

    private void setIsLiked(String is_liked) {
        this.IsLiked = is_liked;
    }
    public String getIsLiked() {
        return IsLiked;
    }

    public String getnCompanyAddress() {
        return nCompanyAddress;
    }

    private void setnCompanyAddress(String nCompanyAddress) {
        this.nCompanyAddress = nCompanyAddress;
    }

    public String getnLocation() {
        return nLocation;
    }

    public void setnLocation(String nLocation) {
        this.nLocation = nLocation;
    }

    public String getnStatus() {
        return nStatus;
    }

    public void setnStatus(String nStatus) {
        this.nStatus = nStatus;
    }

    public String getnDesignation() {
        return nDesignation;
    }

    private void setnDesignation(String nDesignation) {
        this.nDesignation = nDesignation;
    }

    public String getnFileName() {
        return nFileName;
    }

    private void setnFileName(String nFileName) {
        this.nFileName = nFileName;
    }

    public String getnFileType() {
        return nFileType;
    }

    private void setnFileType(String nFileType) {
        this.nFileType = nFileType;
    }

    public String getnMessage() {
        return nMessage;
    }

    private void setnMessage(String nMessage) {
        this.nMessage = nMessage;
    }

    public String getnId() {
        return nId;
    }

    private void setnId(String nId) {
        this.nId = nId;
    }

    public String getnUserId() {
        return nUserId;
    }

    private void setnUserId(String nUserId) {
        this.nUserId = nUserId;
    }

    public String getnBusinessNature() {
        return nBusinessNature;
    }

    public void setnBusinessNature(String nBusinessNature) {
        this.nBusinessNature = nBusinessNature;
    }

    public String getnCompanyName() {
        return nCompanyName;
    }

    private void setnCompanyName(String nCompanyName) {
        this.nCompanyName = nCompanyName;
    }

    public String getnTitle() {
        return nTitle;
    }

    private void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnDOB() {
        return nDOB;
    }

    public void setnDOB(String nDOB) {
        this.nDOB = nDOB;
    }

    public String getnFname() {
        return nFname;
    }

    private void setnFname(String nFname) {
        this.nFname = nFname;
    }

    public String getnLname() {
        return nLname;
    }

    private void setnLname(String nLname) {
        this.nLname = nLname;
    }

    public String getnProfilePic() {
        return nProfilePic;
    }

    private void setnProfilePic(String nProfilePic) {
        this.nProfilePic = nProfilePic;
    }

    public String getnAdvertiseImage() {
        return nAdvertiseImage;
    }

    private void setnAdvertiseImage(String nAdvertiseImage) {
        this.nAdvertiseImage = nAdvertiseImage;
    }

    public boolean isnIsAdvertise() {
        return nIsAdvertise;
    }

    private void setnIsAdvertise(boolean nIsAdvertise) {
        this.nIsAdvertise = nIsAdvertise;
    }

    public boolean isnHasProfilePic() {
        return nHasProfilePic;
    }

    private void setnHasProfilePic(boolean nHasProfilePic) {
        this.nHasProfilePic = nHasProfilePic;
    }
}
