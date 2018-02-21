package com.nschirmer.pomodoro.db;


import com.pacoworks.rxpaper2.RxPaperBook;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class PaperHelper {

    static void saveObjectIntoPaper(PaperObjectHelper paperObject, CompletableObserver observer) {
        RxPaperBook database = RxPaperBook.with(paperObject.getBook());
        Completable write = database.write(paperObject.getKey(), paperObject.getObject());
        write.subscribe(observer);
    }


    static void getObjectFromPaper(String book, String key, SingleObserver<Object> singleObserver){
        RxPaperBook database = RxPaperBook.with(book);
        Single<Object> readOrDefault = database.read(key, null);
        readOrDefault.subscribe(singleObserver);
    }


    static void deleteObjectFromPaper(String book, String key, CompletableObserver observer){
        RxPaperBook database = RxPaperBook.with(book);
        Completable delete = database.delete(key);
        delete.subscribe(observer);
    }
}
