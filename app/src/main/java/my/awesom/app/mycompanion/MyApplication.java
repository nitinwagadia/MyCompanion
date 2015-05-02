package my.awesom.app.mycompanion;

import android.app.Application;

/**
 * Created by nitin on 4/18/15.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static MyApplication getInstance() {

        return instance;
    }

}
