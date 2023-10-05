package com.goose.androidtest.database;

public class Problem {
    private long id;
    private String title;
    private String description;
    private String expDate;

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
    public String getExpDate() {
        return expDate;
    }


}
