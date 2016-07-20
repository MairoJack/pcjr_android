package com.pcjinrong.pcjr.common;

import android.app.Application;
import android.content.Context;

import com.pcjinrong.pcjr.model.FocusImg;
import com.pcjinrong.pcjr.model.Announce;

import java.util.List;


/**
 * 一些常量
 * Created by Mario on 2016/5/22.
 */
public class Constant extends Application{
    public static final int ERROR = 0;
    public static final int REQUSET = 1;
    public final static int SAFESETTING = 2;

    public final static int PAGESIZE = 15;              //分页大小
    public final static String BEARER = "Bearer";       //BEARER
    public final static String FILE = "user_data";      //存储文件名
    /**
     * test
     */
    public final static String BASEURL = "http://api.pcjr.test/mapi/";
    public final static String CLIENTID = "1";
    public final static String CLIENTSECRET = "123";
    /**
     * pro
     */
    /*public final static String BASEURL = "https://www.pcjr.com/mapi/";
    public final static String CLIENTID = "JkXitPIFMbhQwjyP6kx5pARvwVbTQD874kv2hbRn";
    public final static String CLIENTSECRET = "41nkgMbf3yJ7UtzWLFxWOtvmDxREGJL4CwjkkAwY";*/
    public static String access_token = "";
    public static String refresh_token = "";
    public static boolean isLogin = false;              //用户登录
    public static boolean isGestureLogin = false;       //手势登录
    public static String DEVICETOKEN;                   // 设备token
    public static String realname;
    public static int TYPE = 0;
    //手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0;     // 正常状态
    public static final int POINT_STATE_SELECTED = 1;   // 按下状态
    public static final int POINT_STATE_WRONG = 2;      // 错误状态

    private List<FocusImg> focusImgs;
    private List<FocusImg> midFocusImgs;
    private List<Announce> announces;
    private int mCounter;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }

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
        refresh_token = "";
        isLogin = false;
    }
}
