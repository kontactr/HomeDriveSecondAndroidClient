package com.example.yash.homedrivesecond.DisplayStorage;

/**
 * Created by Yash on 04-02-2018.
 */

public class FileBean {

    String url , text, size ;
    boolean file;


    public void setFile(boolean file) {
        this.file = file;
    }

    public FileBean(String url, String text, boolean file, String size) {
        this.url = url;
        this.text = text;
        this.file = file;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSize() {
        return size;
    }

    public boolean isFile() {
        return file;
    }

    public void setSize(String size) {

        this.size = size;
    }
}