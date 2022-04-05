package org.story.storyapp;

public class Page {
    String image;
    String record;

    public Page(String image, String record) {
        this.image = image;
        this.record = record;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}