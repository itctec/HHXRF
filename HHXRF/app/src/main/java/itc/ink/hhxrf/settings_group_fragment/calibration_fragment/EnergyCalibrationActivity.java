package itc.ink.hhxrf.settings_group_fragment.calibration_fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.hardware.DataCallBack;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class EnergyCalibrationActivity extends BaseActivity {

    public static boolean isEditState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_energy_calibration);

        MainActivity.hardwareBroadCastReceiver.addCallBack(new DataCallBack() {
            @Override
            public void onDataChanged(String s) {
                if(s.startsWith("B_")){
                    Intent intent=new Intent();
                    intent.setClass(EnergyCalibrationActivity.this, OnEnergyCalibrationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.hardwareBroadCastReceiver.resetCallBack();
    }


    public void onBackBtnClick(View view){
        finish();
    }

    public void onPull(View view){
        Intent intent=new Intent();
        intent.setClass(EnergyCalibrationActivity.this, OnEnergyCalibrationActivity.class);
        startActivity(intent);
        finish();
    }

}
