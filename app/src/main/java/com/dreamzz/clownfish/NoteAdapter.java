package com.dreamzz.clownfish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {


    private ArrayList<Note> notes;
    private Context context;
    private LayoutInflater inflater;

    public NoteAdapter(ArrayList<Note> notes, Context context){
        this.notes= notes;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.item_note, viewGroup, false);
        }
        Note note = getItem(getCount()-i-1);
        TextView text, date;
        text = (TextView) view.findViewById(R.id.note_item_text);
        date = (TextView) view.findViewById(R.id.note_item_date);
        text.setText(note.getText());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        date.setText(dateFormat.format(note.getDate()));
        return view;
    }
}
