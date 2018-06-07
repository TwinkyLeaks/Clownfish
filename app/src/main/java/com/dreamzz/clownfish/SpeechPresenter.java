package com.dreamzz.clownfish;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import ru.yandex.speechkit.Emotion;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Language;
import ru.yandex.speechkit.OnlineModel;
import ru.yandex.speechkit.OnlineRecognizer;
import ru.yandex.speechkit.OnlineVocalizer;
import ru.yandex.speechkit.Quality;
import ru.yandex.speechkit.Recognition;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.RecognizerListener;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Track;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;
import ru.yandex.speechkit.Voice;

/**
 * Created by Artemiy Morozov on 20.04.2018.
 */

public class SpeechPresenter implements RecognizerListener, VocalizerListener {


    public interface OnSpeechEvent {
        void onChangeLanguage(Language lang);

        void onPartialText(String text);

        void onStopRecognize();

        void onError(String error);

        void onSynthesisDone(Vocalizer vocalizer);

        void onPermissionNeed(String[] permissions, int[] modes);

        void onStartRecognize();

        void onExit();
    }

    private OnSpeechEvent callback;
    private OnlineRecognizer recognizer;
    private OnlineVocalizer vocalizer;
    private Language currentLanguage;
    private Context context;
    private boolean recState = true;
    private String TAG = "SpeechPresenter: ";

    public void setRecState(boolean recState) {
        this.recState = recState;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCallback(OnSpeechEvent callback) {
        this.callback = callback;
    }

    public void init() {
        recState = true;
        currentLanguage = Language.RUSSIAN;
        recognizer = new OnlineRecognizer.Builder(currentLanguage, OnlineModel.QUERIES, this)
                .setDisableAntimat(true)
                .setEnablePunctuation(true)
                .build();
        recognizer.prepare();
        vocalizer = new OnlineVocalizer.Builder(currentLanguage, this)
                .setEmotion(Emotion.GOOD)
                .setQuality(Quality.HIGH)
                .setVoice(Voice.ALYSS)
                .build();
        vocalizer.prepare();

    }

    public void changeLanguage() {
        if(recState){
            if (currentLanguage == Language.RUSSIAN) {
                currentLanguage = Language.ENGLISH;
            } else {
                currentLanguage = Language.RUSSIAN;
            }
            recognizer = new OnlineRecognizer.Builder(currentLanguage, OnlineModel.QUERIES, this)
                    .setDisableAntimat(true)
                    .setEnablePunctuation(true)
                    .build();
            recognizer.prepare();
            vocalizer = new OnlineVocalizer.Builder(currentLanguage, this)
                    .setEmotion(Emotion.GOOD)
                    .setQuality(Quality.HIGH)
                    .setVoice(Voice.ALYSS)
                    .build();
            vocalizer.prepare();
            callback.onChangeLanguage(currentLanguage);
        }
    }

    public void startSynthesis(String str) {
        vocalizer.synthesize(str, Vocalizer.TextSynthesizingMode.APPEND);
    }

    public void startRecording() {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionNeed(new String[]{Manifest.permission.RECORD_AUDIO}, new int[]{PackageManager.PERMISSION_GRANTED});
        }else{
            recognizer.startRecording();
            callback.onStartRecognize();
        }

    }

    public void addNote(String text){
        try {
            FileHandler.addNote(new Note(text, new Date(), currentLanguage));
        } catch (IOException e) {
            Log.d(TAG, e.getLocalizedMessage());
            callback.onError(e.getLocalizedMessage());
        }
    }

    public void stopRecording(){
        try {
            recognizer.finalize();
            recognizer = new OnlineRecognizer.Builder(currentLanguage, OnlineModel.QUERIES, this)
                    .setDisableAntimat(true)
                    .setEnablePunctuation(true)
                    .build();
            recognizer.prepare();
            callback.onStopRecognize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Log.d(TAG, "stopRecording: " + throwable.getLocalizedMessage());
            callback.onError(throwable.getLocalizedMessage());
        }
    }


    @Override
    public void onRecordingBegin(@NonNull Recognizer recognizer) {

    }

    @Override
    public void onSpeechDetected(@NonNull Recognizer recognizer) {

    }

    @Override
    public void onSpeechEnds(@NonNull Recognizer recognizer) {

    }

    @Override
    public void onRecordingDone(@NonNull Recognizer recognizer) {

    }

    @Override
    public void onPowerUpdated(@NonNull Recognizer recognizer, float v) {

    }

    @Override
    public void onPartialResults(@NonNull Recognizer recognizer, @NonNull Recognition recognition, boolean b) {
        callback.onPartialText(recognition.getBestResultText());
        if(b){
            callback.onStopRecognize();
        }

    }

    @Override
    public void onRecognitionDone(@NonNull Recognizer recognizer) {
            callback.onStopRecognize();
    }

    @Override
    public void onRecognizerError(@NonNull Recognizer recognizer, @NonNull Error error) {
        callback.onError(error.getMessage());
    }

    @Override
    public void onMusicResults(@NonNull Recognizer recognizer, @NonNull Track track) {

    }

    @Override
    public void onSynthesisDone(@NonNull Vocalizer vocalizer) {
        callback.onSynthesisDone(vocalizer);
    }

    @Override
    public void onPartialSynthesis(@NonNull Vocalizer vocalizer, @NonNull Synthesis synthesis) {

    }

    @Override
    public void onPlayingBegin(@NonNull Vocalizer vocalizer) {

    }

    @Override
    public void onPlayingDone(@NonNull Vocalizer vocalizer) {

    }

    @Override
    public void onVocalizerError(@NonNull Vocalizer vocalizer, @NonNull Error error) {
        callback.onError(error.getCode()+"\n"+error.getMessage());
    }


}
