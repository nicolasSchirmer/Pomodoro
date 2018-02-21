package com.nschirmer.pomodoro.db;


public class PaperObjectHelper {

    private String book, key;
    private Object object;

    public PaperObjectHelper(){
        this.book = null;
        this.key = null;
        this.object = null;
    }

    public PaperObjectHelper(String book, String key, Object object) {
        this.book = book;
        this.key = key;
        this.object = object;
    }

    // return the object to be cleared
    public PaperObjectHelper clear(){
        PaperObjectHelper paperObjectHelper = new PaperObjectHelper(book, key, object);

        book = null;
        key = null;
        object = null;

        return paperObjectHelper;
    }

    public boolean isClean(){
        return book == null && key == null && object == null;
    }

    public String getKey() {
        return key;
    }

    public String getBook() {
        return book;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof PaperObjectHelper) && (key != null)
                ? key.equals(((PaperObjectHelper) object).key)
                : (object == this);
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : super.hashCode();
    }
}
