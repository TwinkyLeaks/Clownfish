package com.dreamzz.clownfish;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

import ru.yandex.speechkit.Emotion;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.OnlineVocalizer;
import ru.yandex.speechkit.Quality;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;
import ru.yandex.speechkit.Voice;

public class NotePresenter implements VocalizerListener {


    public interface OnNotePresenterEvent{
        void onError(String error);
        void onSuccess(ArrayList<Note> notes);
        void onNoteNotFound();
    }

    private OnNotePresenterEvent callback;
    private OnlineVocalizer vocalizer;

    public void setCallback(OnNotePresenterEvent callback) {
        this.callback = callback;
    }

    public void init(){
        ArrayList<Note> notes = FileHandler.getNotes();
        /*ArrayList<Note> foo = new ArrayList<>();
        for (int i = notes.size(); i>=0; i--){
            foo.add(notes.get(i));
        }*/
        if(notes.size() == 0 || notes == null){
            callback.onNoteNotFound();
        }else{
            callback.onSuccess(notes);
        }
    }

    public void vocalize(Note note){
        vocalizer = new OnlineVocalizer.Builder(note.getLanguage(), this)
                .setEmotion(Emotion.GOOD)
                .setQuality(Quality.HIGH)
                .setVoice(Voice.ALYSS)
                .build();
        vocalizer.prepare();
        vocalizer.synthesize(note.getText(), Vocalizer.TextSynthesizingMode.APPEND);
    }

    public void deleteNote(Note note){
        try {
            FileHandler.removeNote(note);
            init();
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e.getLocalizedMessage());
        }
    }

    @Override
    public void onSynthesisDone(@NonNull Vocalizer vocalizer) {

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
        callback.onError(error.getMessage());
    }

}
