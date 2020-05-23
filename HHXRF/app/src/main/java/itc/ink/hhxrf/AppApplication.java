package itc.ink.hhxrf;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.android.print.sdk.util.Utils;

import java.util.Locale;
import java.util.Properties;

import itc.ink.hhxrf.settings_group_fragment.language_fragment.LanguageFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initPrintSDK();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationContext = this;
    }

    //sdk 防止第一次运行程序, 闪退问题
    private void initPrintSDK(){
        try {
            Properties pro = Utils.getBtConnInfo(this);
            if(pro.isEmpty()){
                pro.put("mac", "");
            }
        } catch (Exception e) {
            Utils.saveBtConnInfo(this, "");
        }
    }
}
