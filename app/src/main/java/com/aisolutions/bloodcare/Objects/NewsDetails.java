package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-06-20.
*/

public class NewsDetails {

    String topic;
    String summary;
    String description;
    String imageUrl;
    String date;

    public NewsDetails() {
    }

    public NewsDetails(String topic, String summary, String description, String imageUrl, String date) {
        this.topic = topic;
        this.summary = summary;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
