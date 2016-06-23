package com.pcjinrong.pcjr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 投资记录实体类
 * Created by Mario on 2016/5/26.
 */
public class InvestRecords {
    @Expose
    @SerializedName("product_name")
    private String productName;
    @Expose
    private int repayment;
    @Expose
    private String amount;
    @Expose
    @SerializedName("year_income")
    private String yearIncome;
    @Expose
    @SerializedName("interest_total")
    private String interestTotal;
    @Expose
    @SerializedName("join_date")
    private long joinDate;
    @Expose
    private long deadline;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getRepayment() {
        return repayment;
    }

    public void setRepayment(int repayment) {
        this.repayment = repayment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(String yearIncome) {
        this.yearIncome = yearIncome;
    }

    public String getInterestTotal() {
        return interestTotal;
    }

    public void setInterestTotal(String interestTotal) {
        this.interestTotal = interestTotal;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
