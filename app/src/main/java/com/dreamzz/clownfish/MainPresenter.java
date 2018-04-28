package com.dreamzz.clownfish;

import android.content.Context;
import android.util.Log;

import java.util.UUID;

import ru.yandex.speechkit.SpeechKit;

/**
 * Created by Artemiy Morozov on 17.04.2018.
 */

public class MainPresenter {

    private String  TAG = "MainPresenter";

    public interface ActivityHandler{
        void onNetworkFailed();
        void onSuccess();
        void onSpeechKitException(String error);
        void onLoading();
        void onAuthFailed();
    }

    private ActivityHandler callback;

    public void setCallback(ActivityHandler activityHandler){
        callback = activityHandler;
    }

    public void init(Context appContext){

        try {
            callback.onLoading();
            Log.d(TAG, "init: start init SpeechKit");
            SpeechKit.getInstance().init(appContext, "d4d25a06-90bd-47ce-8e90-1077b9a6b9e1");
            SpeechKit.getInstance().setUuid(UUID.randomUUID().toString());


            Log.d(TAG, "init: successful init SpeechKit");
            callback.onSuccess();
        } catch (SpeechKit.LibraryInitializationException e) {
            Log.d(TAG, e.getLocalizedMessage());
            callback.onSpeechKitException(e.getLocalizedMessage());
        }
    }
}
