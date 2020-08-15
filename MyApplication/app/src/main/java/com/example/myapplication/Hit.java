package com.example.myapplication;

/**
 * Created by Mike Lee on 9/07/2017.
 */

public class Hit {
    private String Title;
    private String url;

    public Hit(String mTitle, String mUrl) {
        Title = mTitle;
        url = mUrl;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return url;
    }


}
