package com.dreamzz.clownfish;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity implements SwipeMenuListView.OnSwipeListener, SwipeMenuListView.OnMenuItemClickListener, View.OnClickListener, NotePresenter.OnNotePresenterEvent, AdapterView.OnItemClickListener {


    private NotePresenter presenter;

    private SwipeMenuListView listView;
    private ProgressBar progressBar;
    private TextView textView;
    private FloatingActionButton startSpeechButton;

    private ArrayList<Note> notes;
    private boolean networkState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        presenter = new NotePresenter();
        presenter.setCallback(this);

        listView = (SwipeMenuListView) findViewById(R.id.note_list_view);
        listView.setMenuCreator(getSwipeMenuCreator());
        listView.setOnSwipeListener(this);
        listView.setOnMenuItemClickListener(this);
        listView.setCloseInterpolator(new BounceInterpolator());
        listView.setOpenInterpolator(new BounceInterpolator());
        textView = (TextView) findViewById(R.id.note_not_found);
        startSpeechButton = (FloatingActionButton) findViewById(R.id.note_add_note);
        startSpeechButton.setOnClickListener(this);

        presenter.init();



    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.init();
    }

    private SwipeMenuCreator getSwipeMenuCreator(){
        return new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem close = new SwipeMenuItem(getApplicationContext());
                close.setBackground(new ColorDrawable(Color.rgb(245, 112, 79)));
                close.setWidth(dp2px(90));
                close.setIcon(resize(getDrawable(R.drawable.remove_second_img)));
                menu.addMenuItem(close);
            }
        };
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void drawListView(){
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        listView.setAdapter(new NoteAdapter(notes, this));
        listView.setOnItemClickListener(this);
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 50, 50, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    @Override
    public void onSwipeStart(int position) {
        startSpeechButton.hide();
    }

    @Override
    public void onSwipeEnd(int position) {
        if(position <= 3){
            startSpeechButton.show();
        }
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        presenter.deleteNote(notes.get(notes.size()-1-position));
        return false;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.note_add_note:
                Intent intent = new Intent(this, SpeechActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccess(ArrayList<Note> notes) {
        this.notes = notes;
        drawListView();
    }

    @Override
    public void onNoteNotFound() {
        textView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.vocalize(notes.get(notes.size()-1-i));
    }
}
