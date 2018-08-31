package adapters;

import android.content.Loader;
import android.inputmethodservice.ExtractEditText;
import android.os.Looper;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class AppExecuters {
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;
    private static final Object LOCK = new Object();
    private static AppExecuters instance;


    private AppExecuters(Executor diskIO, Executor networkIO, Executor mainThread){
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }

    public static AppExecuters getInstance() {
        if (instance == null){
            synchronized (LOCK){
                instance = new AppExecuters(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
            }
        }

        return instance;
    }

    public Executor diskIO(){
        return diskIO;
    }

    public Executor networkIO(){
        return networkIO;
    }

    public Executor mainThread(){
        return mainThread;
    }
}
