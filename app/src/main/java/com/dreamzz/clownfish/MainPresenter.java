package com.dreamzz.clownfish;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import ru.yandex.speechkit.SpeechKit;

/**
 * Created by Artemiy Morozov on 17.04.2018.
 */

public class MainPresenter implements NetworkClient.OnNetworkAnswer{

    private String  TAG = "MainPresenter";



    public interface ActivityHandler{
        void onNetworkFailed();
        void onSuccess();
        void onSpeechKitException(String error);
        void onLoading();
        void onAuthFailed();
    }

    private ActivityHandler callback;
    private NetworkClient networkClient;
    private Context context;

    public void setCallback(ActivityHandler activityHandler){
        callback = activityHandler;
    }

    public void init(Context appContext){
        context = appContext;
        try {
            callback.onLoading();
            Log.d(TAG, "init: start init SpeechKit");
            SpeechKit.getInstance().init(appContext, "d4d25a06-90bd-47ce-8e90-1077b9a6b9e1");
            SpeechKit.getInstance().setUuid(UUID.randomUUID().toString());


            Log.d(TAG, "init: successful init SpeechKit");
                if(hasConnection(appContext)){
                    networkClient = new NetworkClient();
                    networkClient.setCallback(this);
                    networkClient.execute();
                }else{
                    callback.onNetworkFailed();
                }
            FileHandler.init(appContext);

        } catch (SpeechKit.LibraryInitializationException e) {
            Log.d(TAG, e.getLocalizedMessage());
            callback.onSpeechKitException(e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getLocalizedMessage());
            callback.onSpeechKitException(e.getLocalizedMessage());
        }
    }

    public static boolean hasConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess() {
        callback.onSuccess();
    }

    @Override
    public void onFail(int code, String message) {
        callback.onNetworkFailed();
    }

    @Override
    public void onException(String e) {
        callback.onNetworkFailed();
    }


}
