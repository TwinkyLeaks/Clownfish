package com.dreamzz.clownfish;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Artemiy Morozov on 18.04.2018.
 */

public class ErrorFragment extends Fragment{

    private View view;
    private View.OnClickListener onClickListener;
    private Button reload;
    private TextView errorText;
    private  String errorString;


    public void setOnClickListener(View.OnClickListener clickListener){
        onClickListener = clickListener;

    }

    public void setErrorText(String error) {
        errorString = error;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_error, container, false);
        reload = (Button) view.findViewById(R.id.error_fragment_reload_button);
        reload.setOnClickListener(onClickListener);
        errorText = (TextView) view.findViewById(R.id.error_fragment_text_view);
        errorText.setText(errorString);
        return view;
    }
}
