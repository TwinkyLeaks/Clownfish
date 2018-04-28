package com.dreamzz.clownfish;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Artemiy Morozov on 18.04.2018.
 */

public class LoadingFragment extends Fragment {

    private ProgressBar progressBar;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_loading, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_loading_progress_bar);
        return view;
    }
}
