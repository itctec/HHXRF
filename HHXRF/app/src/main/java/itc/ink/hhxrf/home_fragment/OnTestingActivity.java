package itc.ink.hhxrf.home_fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

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

public class OnTestingActivity extends BaseActivity {

    private ImageView on_Testing_Progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_on_testing);

        on_Testing_Progress=findViewById(R.id.on_Testing_Progress);
        t_NativeRoutineAnalysis.start();
    }


    Thread t_NativeRoutineAnalysis=new Thread(){
        @Override
        public void run() {
            super.run();
            HardwareControl.nativeRoutineAnalysis();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    on_Testing_Progress.setImageResource(R.drawable.progress_on_testing_state_three);
                }
            });

            readSummaryDataFromCsv();

            try{
                sleep(200);
            }catch (Exception e){
                System.out.println(e.toString());
            }
            setResult(MainActivity.TESTING_ACTIVITY_RESULT_CODE_OK);
            finish();
        }
    };

    private void readSummaryDataFromCsv() {
        try {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(OnTestingActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            String checkSampleNameSqlStr = "select sample_name from tb_history_data where sample_name like 'Sample%' order by sample_name desc LIMIT 1";
            Cursor cursor = sqLiteDatabase.rawQuery(checkSampleNameSqlStr, null);
            String tempSampleName="Sample";
            if(cursor.moveToNext()){
                tempSampleName+=(Integer.parseInt(cursor.getString(cursor.getColumnIndex("sample_name")).substring(6))+1);
            }else{
                tempSampleName+=1;
            }

            ContentValues newSampleDbValues = new ContentValues();
            newSampleDbValues.put("sample_name", tempSampleName);
            String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
            newSampleDbValues.put("test_datetime", currentDateTimeString);
            newSampleDbValues.put("test_way", SharedPreferenceUtil.getString(TestWayFragment.TEST_WAY_KEY,TestWayFragment.TEST_WAY_VALUE_METAL));
            sqLiteDatabase.insert("tb_history_data", "", newSampleDbValues);



            File csv = new File("data/XRS/Data_T/summary.csv");
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println("Data Line-->" + line);
                String buffer[] = line.split(",");

                ContentValues historyDataContentDbValues = new ContentValues();
                historyDataContentDbValues.put("element_name", buffer[0]);
                historyDataContentDbValues.put("element_concentration", buffer[4]);
                historyDataContentDbValues.put("element_range", "18~19");
                historyDataContentDbValues.put("element_average", "62");
                historyDataContentDbValues.put("sample_name", tempSampleName);
                sqLiteDatabase.insert("tb_history_data_content", "", historyDataContentDbValues);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
