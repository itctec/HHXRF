package itc.ink.hhxrf.home_fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Locale;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.hardware.HardwareControl;
import itc.ink.hhxrf.settings_group_fragment.calibration_fragment.TypeCalibrationActivity;
import itc.ink.hhxrf.settings_group_fragment.test_time_fragment.TestTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class OnTestingActivity extends BaseActivity {
    private TextView on_Testing_Tip_Sub_Text;
    private ImageView on_Testing_Progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_on_testing);

        on_Testing_Tip_Sub_Text=findViewById(R.id.on_Testing_Tip_Sub_Text);
        String timeStrRes=getResources().getString(R.string.home_on_testing_waiting_time_tip);
        String timeStr=String.format(timeStrRes,SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY,15)+7);
        on_Testing_Tip_Sub_Text.setText(timeStr);
        on_Testing_Progress=findViewById(R.id.on_Testing_Progress);
        t_NativeRoutineAnalysis.start();
    }


    Thread t_NativeRoutineAnalysis=new Thread(){
        @Override
        public void run() {
            super.run();
            try{
                sleep(SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY,15)*1000+7*1000);
            }catch (Exception e){
                System.out.println(e.toString());
            }
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
            newSampleDbValues.put("calibration_type", SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY,TypeCalibrationActivity.CA_NONE_VALUE));
            sqLiteDatabase.insert("tb_history_data", "", newSampleDbValues);



            File csv = new File("data/XRS/Data_T/summary.csv");
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                //System.out.println("Data Line-->" + line);
                String buffer[] = line.split(",");

                ContentValues historyDataContentDbValues = new ContentValues();

                String checkCompoundStr="select * from tb_compound_lib_info where compound_element=? and show_state='true' and compound_id<>'-1'";
                Cursor checkCompoundResultCursor=sqLiteDatabase.rawQuery(checkCompoundStr,new String[]{buffer[0].trim()});
                if(checkCompoundResultCursor.moveToNext()){
                    String compoundName=checkCompoundResultCursor.getString(checkCompoundResultCursor.getColumnIndex("compound_name"));
                    System.out.println("化合物---->"+compoundName);
                    historyDataContentDbValues.put("element_name",compoundName);
                    int elementNum=Integer.parseInt(compoundName.substring(compoundName.toUpperCase().indexOf(buffer[0].toUpperCase().trim())+buffer[0].trim().length()+0,compoundName.toUpperCase().indexOf(buffer[0].toUpperCase().trim())+buffer[0].trim().length()+1));
                    System.out.println("化合物中的元素个数---->"+elementNum);
                    if (SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY,TypeCalibrationActivity.CA_NONE_VALUE).equals(TypeCalibrationActivity.CA_NONE_VALUE)){
                        historyDataContentDbValues.put("element_concentration", Float.parseFloat(buffer[4])/elementNum+"");
                    }else{
                        String checkParametersStr="select * from tb_type_calibration_content where type_name=? and element_name=? and element_id<>?";
                        Cursor checkParametersResultCursor = sqLiteDatabase.rawQuery(checkParametersStr, new String[]{SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY,TypeCalibrationActivity.CA_NONE_VALUE),buffer[0].trim(),"-1"});
                        if(checkParametersResultCursor.moveToNext()){
                            historyDataContentDbValues.put("element_concentration", (Float.parseFloat(buffer[4])*Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_multiplication")))+Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_plus"))))/elementNum+"");
                        }
                    }
                }else{
                    //System.out.println("无化合物元素");
                    historyDataContentDbValues.put("element_name",buffer[0]);
                    if (SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY,TypeCalibrationActivity.CA_NONE_VALUE).equals(TypeCalibrationActivity.CA_NONE_VALUE)){
                        historyDataContentDbValues.put("element_concentration", buffer[4]);
                    }else{
                        String checkParametersStr="select * from tb_type_calibration_content where type_name=? and element_name=? and element_id<>?";
                        Cursor checkParametersResultCursor = sqLiteDatabase.rawQuery(checkParametersStr, new String[]{SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY,TypeCalibrationActivity.CA_NONE_VALUE),buffer[0].trim(),"-1"});
                        if(checkParametersResultCursor.moveToNext()){
                            historyDataContentDbValues.put("element_concentration", Float.parseFloat(buffer[4])*Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_multiplication")))+Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_plus"))));
                        }
                    }
                }


                historyDataContentDbValues.put("element_range", "18~90");
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
