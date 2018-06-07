package com.dreamzz.clownfish;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileHandler {
    private static ArrayList<Note> notes;
    private static File file, dir;
    private static Context context;
    private static Type itemsArrType;

    private static final String filename = "ntp.txt";


    public static void init(Context appContext) throws IOException {
        context = appContext;
        notes = new ArrayList<>();
        itemsArrType = new TypeToken<ArrayList<Note>>() {}.getType();
        /*file = null;
        String sdState = android.os.Environment.getExternalStorageState();
        if(sdState.equals(Environment.MEDIA_MOUNTED)){
            dir = new File(android.os.Environment.getExternalStorageDirectory(), "cache/");
            file = new File(dir, "notes.json");
        }else{
            dir = new File(context.getCacheDir(), "cache/");
            file = new File(dir, "notes.json");
        }
        if(!file.exists()){
            dir.mkdirs();
            file.createNewFile();
        }*/
        openFile();
    }

    private static void saveFile() throws IOException {
        /*context.deleteFile(file.toString());
        if(!file.exists()){
            file.mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(file.getAbsolutePath().toString()), true)));
        bw.write(new Gson().toJson(notes));
        bw.flush();
        bw.close();*/
        FileOutputStream fos = null;
        fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(new Gson().toJson(notes).getBytes());
        fos.flush();
        fos.close();
    }

    private static void openFile() throws IOException {
/*
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(file.getAbsolutePath().toString()))));
        String str, foo = "";
        while ((str = br.readLine()) != null) {
            foo+=str;
        }*/
        FileInputStream fin = null;
        fin = context.openFileInput(filename);
        byte[] bytes = new byte[fin.available()];
        fin.read(bytes);
        fin.close();
        String text = new String (bytes);
        notes = new Gson().fromJson(text, itemsArrType);

    }

    public static ArrayList<Note> getNotes(){
        return notes;
    }

    public static void addNote(Note note) throws IOException {
        notes.add(note);
        saveFile();
    }

    public static void removeNote(Note note) throws IOException {
        notes.remove(note);
        saveFile();
    }



}
