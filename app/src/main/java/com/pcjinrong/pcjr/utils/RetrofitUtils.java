package com.pcjinrong.pcjr.utils;

import com.pcjinrong.pcjr.action.TokenAuthenticator;
import com.pcjinrong.pcjr.common.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
                OkHttpClient.Builder client = new OkHttpClient.Builder();
                client.authenticator(new TokenAuthenticator());
                client.readTimeout(7, TimeUnit.SECONDS);
                client.connectTimeout(7,TimeUnit.SECONDS);
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client.build())
                            .build();
                }
            }
        }
        return retrofit.create(clazz);
    }

    public static <T> T createRefreshApi(Class<T> clazz) {
        if (retrofit == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit.create(clazz);
    }
}
