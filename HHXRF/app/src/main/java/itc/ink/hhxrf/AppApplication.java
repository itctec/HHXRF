package itc.ink.hhxrf;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationContext = this;
    }
}
