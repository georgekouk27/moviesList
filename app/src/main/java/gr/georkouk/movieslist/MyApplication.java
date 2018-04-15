package gr.georkouk.movieslist;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
    }

    public Context getAppContext() {
        return getApplicationContext();
    }

}
