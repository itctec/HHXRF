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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        if(SharedPreferenceUtil.getInt(LanguageFragment.LANGUAGE_KEY)==LanguageFragment.LANGUAGE_VALUE_CHINESE){
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/SourceHanSansCN-Normalx.otf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/Helveticax.ttc")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );

            configuration.locale = Locale.ENGLISH;
        }
        getResources().updateConfiguration(configuration,displayMetrics);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
