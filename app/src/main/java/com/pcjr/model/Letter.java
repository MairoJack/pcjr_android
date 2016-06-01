package com.pcjr.model;

/**
 * 站内信
 * Created by Mario on 2016/6/1.
 */
public class Letter {
    private String id;
    private String title;
    private long send_date;
    private int read_status;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSend_date() {
        return send_date;
    }

    public void setSend_date(long send_date) {
        this.send_date = send_date;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }
}
