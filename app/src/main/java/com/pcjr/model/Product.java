package com.pcjr.model;

import com.google.gson.annotations.Expose;

/**
 * 产品类
 */
public class Product {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String year_income;
    @Expose
    private String amount;
    @Expose
    private String month;
    @Expose
    private String repayment;
    @Expose
    private int status;
    @Expose
    private long pub_date;
    @Expose
    private String series;
    @Expose
    private int is_preview_repayment;
    @Expose
    private int rate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear_income() {
        return year_income;
    }

    public void setYear_income(String year_income) {
        this.year_income = year_income;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPub_date() {
        return pub_date;
    }

    public void setPub_date(long pub_date) {
        this.pub_date = pub_date;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getIs_preview_repayment() {
        return is_preview_repayment;
    }

    public void setIs_preview_repayment(int is_preview_repayment) {
        this.is_preview_repayment = is_preview_repayment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}