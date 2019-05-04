package itc.ink.hhxrf;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

import itc.ink.hhxrf.settings_group_fragment.language_fragment.LanguageFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

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
