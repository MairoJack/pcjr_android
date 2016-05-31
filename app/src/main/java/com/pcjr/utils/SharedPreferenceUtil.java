package com.pcjr.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mario on 2016/5/26.
 */
public class SharedPreferenceUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtil(Context context, String file) {
        this.sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
        editor = this.sp.edit();
    }

    public void setUsername(String username) {
        this.editor.putString("username", username);
        this.editor.commit();
    }

    public String getUsernam() {
        return this.sp.getString("username", "");
    }

    public void setAccessToken(String accessToken) {
        this.editor.putString("accessToken", accessToken);
        this.editor.commit();
    }

    public String getAccessToken() {
        return this.sp.getString("accessToken", "");
    }

    public void setRefresToken(String refresToken) {
        this.editor.putString("refresToken", refresToken);
        this.editor.commit();
    }

    public String getRefresToken() {
        return this.sp.getString("refresToken", "");
    }

    public void setPassword(String password) {
        this.editor.putString("password", password);
        this.editor.commit();
    }

    public String getPassword() {
        return this.sp.getString("password", "");
    }

    public void setIsFirst(boolean isFirst) {
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public boolean getisFirst() {
        return sp.getBoolean("isFirst", true);
    }

    public void clear(){
        this.setIsFirst(true);
        this.setAccessToken("");
        this.setAccessToken("");
        this.setUsername("");
        this.setPassword("");
    }
}
