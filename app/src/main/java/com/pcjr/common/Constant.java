package com.pcjr.common;

import android.app.Application;

import com.pcjr.model.Announce;
import com.pcjr.model.FocusImg;

import java.util.List;


/**
 * 一些常量
 * Created by Mario on 2016/5/22.
 */
public class Constant extends Application{

    public final static int PAGESIZE = 15;              //分页大小
    public final static String BEARER = "Bearer";       //BEARER
    public final static String FILE = "user_data";      //存储文件名
    public final static String CLIENTID = "1";
    public final static String CLIENTSECRET = "123";
    public static String access_token;
    public static boolean isLogin = false;              //用户登录
    public static boolean isGestureLogin = false;       //手势登录
    public static String DEVICETOKEN;                   // 设备token
    public static String realname;

    //手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0;     // 正常状态
    public static final int POINT_STATE_SELECTED = 1;   // 按下状态
    public static final int POINT_STATE_WRONG = 2;      // 错误状态

    private List<FocusImg> focusImgs;
    private List<FocusImg> midFocusImgs;
    private List<Announce> announces;
    private int mCounter;

    public List<FocusImg> getFocusImgs() {
        return focusImgs;
    }

    public void setFocusImgs(List<FocusImg> focusImgs) {
        this.focusImgs = focusImgs;
    }

    public List<FocusImg> getMidFocusImgs() {
        return midFocusImgs;
    }

    public void setMidFocusImgs(List<FocusImg> midFocusImgs) {
        this.midFocusImgs = midFocusImgs;
    }

    public List<Announce> getAnnounces() {
        return announces;
    }

    public void setAnnounces(List<Announce> announces) {
        this.announces = announces;
    }

    public int getmCounter() {
        return mCounter;
    }

    public void setmCounter(int mCounter) {
        this.mCounter = mCounter;
    }

    public static void clear(){
        access_token = "";
        isLogin = false;
    }
}
