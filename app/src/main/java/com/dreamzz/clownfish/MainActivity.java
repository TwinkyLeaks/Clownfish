package com.dreamzz.clownfish;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static com.dreamzz.clownfish.R.id.activity_main_fragment_container;


public class MainActivity extends AppCompatActivity implements MainPresenter.ActivityHandler, View.OnClickListener{

    private  String TAG = "Main Activity";
    private MainPresenter presenter;
    private android.app.FragmentTransaction transaction;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: created main layout");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.getApplicationContext().checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(new String[]{Manifest.permission.INTERNET}, 200);
            }
        }

        presenter = new MainPresenter();
        presenter.setCallback(this);
        presenter.init(this.getApplicationContext());


    }

    private void clearContainer(){
        transaction = getFragmentManager().beginTransaction();
        if(!transaction.isEmpty() || currentFragment!=null){
            transaction.remove(currentFragment);
        }
        transaction.commit();
    }

    @Override
    public void onNetworkFailed() {
        clearContainer();
        transaction = getFragmentManager().beginTransaction();
        ErrorFragment fragment = new ErrorFragment();
        fragment.setOnClickListener(this);
        transaction.add(activity_main_fragment_container, fragment);
        fragment.setErrorText("Что-то пошло не так");
        currentFragment = fragment;
        transaction.commit();
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(this, SpeechActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSpeechKitException(String error) {
        clearContainer();
        transaction = getFragmentManager().beginTransaction();
        ErrorFragment fragment = new ErrorFragment();
        fragment.setOnClickListener(this);
        fragment.setErrorText(error);
        transaction.add(activity_main_fragment_container, fragment);


        currentFragment = fragment;
        transaction.commit();
    }

    @Override
    public void onLoading() {
        clearContainer();
        transaction = getFragmentManager().beginTransaction();
        LoadingFragment fragment = new LoadingFragment();
        transaction.add(activity_main_fragment_container, fragment);
        currentFragment = fragment;
        transaction.commit();

    }

    @Override
    public void onAuthFailed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.error_fragment_reload_button:
                presenter.init(this.getApplicationContext());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.init(getApplicationContext());
    }
}
