package com.dreamzz.clownfish;

import java.util.Date;

import ru.yandex.speechkit.Language;

public class Note {
    private String text;
    private Date date;
    private Language language;

    public Note(){
        text = null;
        date = null;
    }

    public Note(String text, Date date, Language language){
        this.text = text;
        this.date = date;
        this.language = language;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }
}
