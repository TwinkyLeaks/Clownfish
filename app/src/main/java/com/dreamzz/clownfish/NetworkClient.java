package com.dreamzz.clownfish;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Artemiy Morozov on 28.04.2018.
 */

public class NetworkClient extends AsyncTask<Void, Void, Void>{

    public interface OnNetworkAnswer{
        void onSuccess();
        void onFail(int code, String message);
        void onException(String e);
    }

    private OnNetworkAnswer callback;
    private OkHttpClient client;
    private static final String TAG = "NetworkClient";


    public void setCallback(OnNetworkAnswer callback) {
        this.callback = callback;
    }



    @Override
    protected Void doInBackground(Void... params) {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .build();
        try {
            Response response = client.newCall(
                    new Request.Builder()
                    .get()
                    .url("https://yandex.ru/")
                    .build()).execute();
            ;
            if (response.isSuccessful()){
               callback.onSuccess();
                Log.d(TAG, "doInBackground: access to yandex services granted");
            }else{
                callback.onFail(response.code(), response.message());
                Log.d(TAG, "doInBackground: " + response.code() + "\n" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onException(e.getLocalizedMessage());
            Log.d(TAG, "doInBackground: " + e.getLocalizedMessage());
        }
        return null;
    }
}
