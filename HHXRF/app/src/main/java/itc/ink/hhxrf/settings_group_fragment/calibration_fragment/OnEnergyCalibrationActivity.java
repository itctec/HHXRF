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
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class OnEnergyCalibrationActivity extends BaseActivity {

    private ImageView on_Energy_Calibration_Progress;
    private TextView waitingTimeTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_on_energy_calibration);

        on_Energy_Calibration_Progress=findViewById(R.id.on_Energy_Calibration_Progress);
        waitingTimeTip=findViewById(R.id.on_Energy_Calibration_Tip_Sub_Text);

        t_NativeEnergyCalibration.start();
    }


    Thread t_NativeEnergyCalibration=new Thread(){
        @Override
        public void run() {
            super.run();
            HardwareControl.nativeEnergyCalibration();

            try{
                sleep(200);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        on_Energy_Calibration_Progress.setImageResource(R.drawable.progress_on_testing_state_two);
                    }
                });

                sleep(300);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        on_Energy_Calibration_Progress.setImageResource(R.drawable.progress_on_testing_state_three);
                        waitingTimeTip.setText(R.string.on_energy_calibration_waiting_time_finish_tip);
                    }
                });
                sleep(300);
            }catch (Exception e){
                System.out.println(e.toString());
            }

            Intent intent=new Intent();
            intent.setClass(OnEnergyCalibrationActivity.this,EnergyCalibrationResultActivity.class);
            startActivity(intent);

            finish();
        }
    };

}
