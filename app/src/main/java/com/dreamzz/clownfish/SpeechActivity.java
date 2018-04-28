package com.dreamzz.clownfish;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.yandex.speechkit.Language;
import ru.yandex.speechkit.Vocalizer;

public class SpeechActivity extends AppCompatActivity implements SpeechPresenter.OnSpeechEvent, View.OnClickListener {

    private Button recButton, speechButton, changeLanguageButton;
    private boolean recordState;
    private TextView textView;
    private SpeechPresenter presenter;
    private String TAG = "SpeechActivity: ";
    private static int REQUEST_PERMISSION_CODE = 31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        recordState = false;
        presenter = new SpeechPresenter();
        presenter.setContext(this);
        presenter.setCallback(this);
        presenter.init();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                super.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
            }

        }
        textView = (TextView) findViewById(R.id.note_text_view);
        textView.setText("");
        recButton = (Button) findViewById(R.id.speech_rec_button);
        recButton.setOnClickListener(this);
        speechButton = (Button) findViewById(R.id.speech_play_button);
        speechButton.setOnClickListener(this);
        changeLanguageButton = (Button) findViewById(R.id.speech_lang_button);
        changeLanguageButton.setOnClickListener(this);

    }

    @Override
    public void onChangeLanguage(Language lang) {
        if(lang == Language.RUSSIAN){
            changeLanguageButton.setBackground(getResources().getDrawable(R.drawable.language_russian_img, getTheme()));
        }else if(lang == Language.ENGLISH){
            changeLanguageButton.setBackground(getResources().getDrawable(R.drawable.language_english_img, getTheme()));
        }
    }

    @Override
    public void onPartialText(String text) {
        textView.setText(text);
    }

    @Override
    public void onStopRecognize() {
        recButton.setBackground(getResources().getDrawable(R.drawable.start_record_voice_img, getTheme()));
        recordState = false;
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        recButton.setBackground(getResources().getDrawable(R.drawable.start_record_voice_img, getTheme()));
        recordState = false;
    }

    @Override
    public void onSynthesisDone(Vocalizer vocalizer) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPermissionNeed(String[] permissions, int[] modes) {
        super.requestPermissions(permissions, REQUEST_PERMISSION_CODE);
    }


    @Override
    public void onStartRecognize() {recButton.setBackground(getResources().getDrawable(R.drawable.stop_record_voice_img, getTheme()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speech_rec_button:
                Log.d(TAG, "onClick: rec_button " + recordState);
                if(recordState){
                    presenter.stopRecording();
                    recordState = false;
                }else{
                    presenter.startRecording();
                    recordState = true;
                }
                break;
            case R.id.speech_play_button:
                if(textView.getText().toString().compareTo("") != 0){
                    presenter.startSynthesis(textView.getText().toString());
                }
                break;
            case R.id.speech_lang_button:
                presenter.changeLanguage();
                break;
        }
    }
}
