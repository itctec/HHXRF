package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.hardware.HardwareControl;
import itc.ink.hhxrf.settings_group_fragment.test_time_fragment.TestTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;
import itc.ink.hhxrf.view.TestingProgressView;

public class OnEnergyCalibrationActivity extends BaseActivity {

    private TestingProgressView on_Energy_Calibration_Progress_View;
    private TextView waitingTimeTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_on_energy_calibration);

        on_Energy_Calibration_Progress_View=findViewById(R.id.on_Energy_Calibration_Progress_View);
        on_Energy_Calibration_Progress_View.setTotalTime(60 * 1000, new TestingProgressView.ProgressCallBack() {
            @Override
            public void progressEnd() {
                t_NativeEnergyCalibration.start();
            }
        });

        waitingTimeTip=findViewById(R.id.on_Energy_Calibration_Tip_Sub_Text);
    }


    Thread t_NativeEnergyCalibration=new Thread(){
        @Override
        public void run() {
            super.run();
            HardwareControl.nativeEnergyCalibration();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    waitingTimeTip.setText(R.string.on_energy_calibration_waiting_time_finish_tip);
                }
            });
            Intent intent=new Intent();
            intent.setClass(OnEnergyCalibrationActivity.this,EnergyCalibrationResultActivity.class);
            startActivity(intent);

            finish();
        }
    };

}
