package com.example.navigation;

public class News {
    private String title;
    private String description;
    private String urlToImage; // URL gambar dari internet
    private String url;        // Link ke artikel asli

    public News(String title, String description, String urlToImage, String url) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.url = url;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrlToImage() { return urlToImage; }
    public String getUrl() { return url; }
}
