package com.nschirmer.pomodoro.db;

import android.support.annotation.Nullable;
import android.util.Log;

import com.nschirmer.pomodoro.model.PomodoroTask;
import com.nschirmer.pomodoro.util.Dictionary;
import com.pacoworks.rxpaper2.RxPaperBook;

import java.util.ArrayList;
import java.util.List;


import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

import static com.nschirmer.pomodoro.util.Dictionary.*;

public class HelperDB extends PaperHelper {

    /**
     *****************  SAVE  *****************
     * **/

    public interface SaveListener {
        void onComplete();
    }

    /** @param object
     *
     * save whatever object that you want into Paper DB
     * **/
    public static void saveIntoDB(Object object){
        saveIntoDB(object, null);
    }

    /** @param saveListener
     *
     * you can know when the saving process ended with this listener
     * **/
    public static void saveIntoDB(Object object, SaveListener saveListener){
        if(object instanceof PomodoroTask){
            saveObjectToDB(
                    BOOK_KEY_POMODOROTASK,
                    ((PomodoroTask) object).getID(),
                    object,
                    saveListener
            );
        }
    }

    /**
     * Save the object using {@link PaperObjectHelper}
     * to organize all the data that {@link PaperHelper}
     * will need to save the object
     * **/
    private static void saveObjectToDB(String bookKey, String key, Object object, @Nullable final SaveListener saveListener){
        saveObjectIntoPaper(new PaperObjectHelper(bookKey, key, object), new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onComplete() {
                if(saveListener != null) saveListener.onComplete();
            }

            @Override
            public void onError(Throwable e) {}
        });
    }


    /**
     *****************  READ  *****************
     * **/

    public interface PomodoroReaderListener {
        void finishReading(PomodoroTask pomodoroTask);
    }

    public interface PomodoroListReaderListener {
        void finishReading(List<PomodoroTask> pomodoroTasks);
    }

    /** @param pomodoroReaderListener
     *
     * when the paper finish to read all the data you can catch the object with this listener
     * **/
    public static void readPomodoroTaskFromDB(String pomodoroKey, final PomodoroReaderListener pomodoroReaderListener){
        getObjectFromPaper(BOOK_KEY_POMODOROTASK, pomodoroKey, new DisposableSingleObserver<Object>() {
            @Override
            public void onSuccess(Object object) {
                pomodoroReaderListener.finishReading((PomodoroTask) object);
            }

            @Override
            public void onError(Throwable e) {}
        });
    }


    /** @return all keys saved on the PomomodoroTask Book **/
    public static List<String> getAllPomodoroTasksKeysFromDB(){
        return RxPaperBook.with(BOOK_KEY_POMODOROTASK).keys().blockingGet();
    }


    // TODO temporary
    public static List<PomodoroTask> getAllPomodoroTasksFromDB(){
        List<PomodoroTask> pomodoroTasks = new ArrayList<>();
        for(String key : getAllPomodoroTasksKeysFromDB()){
            pomodoroTasks.add((PomodoroTask) Paper.book(BOOK_KEY_POMODOROTASK).read(key));
        }

        return pomodoroTasks;
    }


    // TODO fix
//    private static List<String> keysToRead = new ArrayList<>();
//    public static void getAllPomodoroTasksFromDB(PomodoroListReaderListener pomodoroListReaderListener){
//        keysToRead.clear();
//        keysToRead = getAllPomodoroTasksKeysFromDB();
//        getPomodoroTasksFromPaper(new ArrayList<PomodoroTask>(), pomodoroListReaderListener);
//    }
//
//
//    private static void getPomodoroTasksFromPaper(final List<PomodoroTask> pomodoroTasks, final PomodoroListReaderListener pomodoroListReaderListener){
//        if(! keysToRead.isEmpty()){
//            final String key = keysToRead.get(keysToRead.size() - 1);
//
//            readPomodoroTaskFromDB(key, new PomodoroReaderListener() {
//                @Override
//                public void finishReading(PomodoroTask pomodoroTask) {
//                    pomodoroTasks.add(pomodoroTask);
//                    keysToRead.remove(key);
//                    getPomodoroTasksFromPaper(pomodoroTasks, pomodoroListReaderListener);
//                }
//            });
//
//        } else {
//            pomodoroListReaderListener.finishReading(pomodoroTasks);
//        }
//    }


    /**
     *****************  DELETE  *****************
     * **/
    public static void deletePomodoroTaskFromDB(String pomodoroKey){
        deleteObjectFromPaper(BOOK_KEY_POMODOROTASK, pomodoroKey, new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {}
        });
    }


}
