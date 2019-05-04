package itc.ink.hhxrf;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.util.Locale;

import itc.ink.hhxrf.settings_group_fragment.language_fragment.LanguageFragment;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;

public class BaseActivity extends Activity {
    public static Context baseActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseActivityContext=this;

        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        if(SharedPreferenceUtil.getInt(LanguageFragment.LANGUAGE_KEY)==LanguageFragment.LANGUAGE_VALUE_CHINESE){
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            configuration.locale = Locale.ENGLISH;
        }
        getResources().updateConfiguration(configuration,displayMetrics);
    }

}
