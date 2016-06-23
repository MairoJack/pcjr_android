package com.pcjinrong.pcjr.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mario on 2016/5/12.
 */
public class RetrofitUtils {
    private static Retrofit retrofit;

    public static <T> T createApi(Class<T> clazz) {
        if (retrofit == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            //.baseUrl("https://www.pcjr.com/mapi/")
                            .baseUrl("http://api.pcjr.test/mapi/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit.create(clazz);
    }
}
