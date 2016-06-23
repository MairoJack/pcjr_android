package com.pcjinrong.pcjr.plugins;

import android.content.Context;

/**
 * Created by Mario on 2016/6/20.
 */
public class IosDialog {
    public static void show(String title, Context context){
        new AlertView(title,null, null, new String[]{"好"}, null, context,
                AlertView.Style.Alert, null).show();
    }

    public static void show(String title,String msg, Context context){
        new AlertView(title,msg, null, new String[]{"好"}, null, context,
                AlertView.Style.Alert, null).show();
    }

}
