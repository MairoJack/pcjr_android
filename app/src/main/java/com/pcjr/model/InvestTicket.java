package com.pcjr.model;

import com.google.gson.annotations.Expose;

/**
 * 投资券
 * Created by Mario on 2016/6/2.
 */
public class InvestTicket {
    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String amount;
    @Expose
    private String reach_amount;
    @Expose
    private String is_used;
    @Expose
    private long end_time;
    @Expose
    private Activity activity;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReach_amount() {
        return reach_amount;
    }

    public void setReach_amount(String reach_amount) {
        this.reach_amount = reach_amount;
    }

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    class Activity{
        @Expose
        private String id;
        @Expose
        private String introduction;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
