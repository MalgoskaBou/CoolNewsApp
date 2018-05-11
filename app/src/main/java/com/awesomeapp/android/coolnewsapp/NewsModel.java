package com.awesomeapp.android.coolnewsapp;

public class NewsModel {

    private String articleTitle;
    private String sectionName;
    private String authorName;
    private String dateOfCreate;
    private String webUrl;

    NewsModel(String articleTitle, String sectionName, String authorName, String dateOfCreate, String webUrl) {
        this.articleTitle = articleTitle;
        this.sectionName = sectionName;
        this.authorName = authorName;
        this.dateOfCreate = dateOfCreate;
        this.webUrl = webUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDateOfCreate() {
        return dateOfCreate;
    }

    public String getWebUrl() {
        return webUrl;
    }
}