package itc.ink.hhxrf.home_fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.MainActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.hardware.HardwareControl;
import itc.ink.hhxrf.settings_group_fragment.calibration_fragment.TypeCalibrationActivity;
import itc.ink.hhxrf.settings_group_fragment.format_fragment.FormatFragment;
import itc.ink.hhxrf.settings_group_fragment.test_time_fragment.TestTimeFragment;
import itc.ink.hhxrf.settings_group_fragment.test_way_fragment.TestWayFragment;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.SharedPreferenceUtil;
import itc.ink.hhxrf.utils.StatusBarUtil;
import itc.ink.hhxrf.view.TestingProgressView;

public class OnTestingActivity extends BaseActivity {
    private TextView on_Testing_Tip_Sub_Text;
    private TestingProgressView on_Testing_Progress_View;
    public static int currentTestNum = SharedPreferenceUtil.getString(FormatFragment.FORMAT_KEY, FormatFragment.FORMAT_VALUE_HANLINAG).equals(FormatFragment.FORMAT_VALUE_AVE_PAIHAO) ? 0 : -1;
    private static String groupName = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        Message msg = MainActivity.mHandler.obtainMessage();
        msg.what = 0x02;
        MainActivity.mHandler.dispatchMessage(msg);

        setContentView(R.layout.activity_on_testing);

        on_Testing_Tip_Sub_Text = findViewById(R.id.on_Testing_Tip_Sub_Text);
        String timeStrRes = getResources().getString(R.string.home_on_testing_waiting_time_tip);
        String timeStr = String.format(timeStrRes, SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY, 15) + 3);
        on_Testing_Tip_Sub_Text.setText(timeStr);
        on_Testing_Progress_View = findViewById(R.id.on_Testing_Progress_View);
        on_Testing_Progress_View.setTotalTime(SharedPreferenceUtil.getInt(TestTimeFragment.TEST_TIME_KEY, 15) * 1000 + 3 * 1000, new TestingProgressView.ProgressCallBack() {
            @Override
            public void progressEnd() {
                t_NativeRoutineAnalysis.start();
            }
        });

    }


    Thread t_NativeRoutineAnalysis = new Thread() {
        @Override
        public void run() {
            super.run();
            //调试：此注释必须打开
            HardwareControl.nativeRoutineAnalysis();
            if (currentTestNum == 0) {
                groupName = System.currentTimeMillis() + "";
            }
            currentTestNum = (currentTestNum + 1) % SharedPreferenceUtil.getInt(FormatFragment.FORMAT_VALUE_AVE_PAIHAO, 1);
            readSummaryDataFromCsv();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setResult(MainActivity.TESTING_ACTIVITY_RESULT_CODE_OK);
                    finish();
                }
            });

        }
    };

    private void readSummaryDataFromCsv() {
        try {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(OnTestingActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            String checkSampleNameSqlStr = "select sample_name from tb_history_data";
            Cursor cursor = sqLiteDatabase.rawQuery(checkSampleNameSqlStr, null);
            int maxNum = 0;
            String tempSampleName = "Sample";
            while (cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("sample_name")).substring(6)) > maxNum) {
                    maxNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("sample_name")).substring(6));
                }
            }
            maxNum += 1;
            tempSampleName += maxNum;

            System.out.println("样品名称---->" + tempSampleName);

            ContentValues newSampleDbValues = new ContentValues();
            newSampleDbValues.put("sample_name", tempSampleName);
            String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
            newSampleDbValues.put("test_datetime", currentDateTimeString);
            newSampleDbValues.put("test_way", SharedPreferenceUtil.getString(TestWayFragment.TEST_WAY_KEY, TestWayFragment.TEST_WAY_VALUE_METAL));
            newSampleDbValues.put("calibration_type", SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY, TypeCalibrationActivity.CA_NONE_VALUE));
            sqLiteDatabase.insert("tb_history_data", "", newSampleDbValues);


            File csv = new File("data/XRS/Data_T/summary.csv");
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                //System.out.println("Data Line-->" + line);
                String buffer[] = line.split(",");

                ContentValues historyDataContentDbValues = new ContentValues();

                String checkCompoundStr = "select * from tb_compound_lib_info where compound_element=? and show_state='true' and compound_id<>'-1'";
                Cursor checkCompoundResultCursor = sqLiteDatabase.rawQuery(checkCompoundStr, new String[]{buffer[0].trim()});
                if (checkCompoundResultCursor.moveToNext()) {
                    String compoundName = checkCompoundResultCursor.getString(checkCompoundResultCursor.getColumnIndex("compound_name"));
                    System.out.println("化合物---->" + compoundName);
                    historyDataContentDbValues.put("element_name", compoundName);
                    int elementNum = Integer.parseInt(compoundName.substring(compoundName.toUpperCase().indexOf(buffer[0].toUpperCase().trim()) + buffer[0].trim().length() + 0, compoundName.toUpperCase().indexOf(buffer[0].toUpperCase().trim()) + buffer[0].trim().length() + 1));
                    System.out.println("化合物中的元素个数---->" + elementNum);
                    if (SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY, TypeCalibrationActivity.CA_NONE_VALUE).equals(TypeCalibrationActivity.CA_NONE_VALUE)) {
                        historyDataContentDbValues.put("element_concentration", Float.parseFloat(buffer[4]) / elementNum);
                    } else {
                        String checkParametersStr = "select * from tb_type_calibration_content where type_name=? and element_name=? and element_id<>?";
                        Cursor checkParametersResultCursor = sqLiteDatabase.rawQuery(checkParametersStr, new String[]{SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY, TypeCalibrationActivity.CA_NONE_VALUE), buffer[0].trim(), "-1"});
                        if (checkParametersResultCursor.moveToNext()) {
                            historyDataContentDbValues.put("element_concentration", (Float.parseFloat(buffer[4]) * Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_multiplication"))) + Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_plus")))) / elementNum);
                        }
                    }
                } else {
                    //System.out.println("无化合物元素");
                    historyDataContentDbValues.put("element_name", buffer[0]);
                    if (SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY, TypeCalibrationActivity.CA_NONE_VALUE).equals(TypeCalibrationActivity.CA_NONE_VALUE)) {
                        historyDataContentDbValues.put("element_concentration", buffer[4]);
                    } else {
                        String checkParametersStr = "select * from tb_type_calibration_content where type_name=? and element_name=? and element_id<>?";
                        Cursor checkParametersResultCursor = sqLiteDatabase.rawQuery(checkParametersStr, new String[]{SharedPreferenceUtil.getString(TypeCalibrationActivity.CA_KEY, TypeCalibrationActivity.CA_NONE_VALUE), buffer[0].trim(), "-1"});
                        if (checkParametersResultCursor.moveToNext()) {
                            historyDataContentDbValues.put("element_concentration", Float.parseFloat(buffer[4]) * Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_multiplication"))) + Integer.parseInt(checkParametersResultCursor.getString(checkParametersResultCursor.getColumnIndex("value_plus"))));
                        }
                    }
                }


                //historyDataContentDbValues.put("element_range", "18~90");

                float element_average = historyDataContentDbValues.getAsFloat("element_concentration");
                if (SharedPreferenceUtil.getString(FormatFragment.FORMAT_KEY).equals(FormatFragment.FORMAT_VALUE_AVE_PAIHAO)) {
                    historyDataContentDbValues.put("group_name", groupName);
                    String calAvgStr = "select avg(element_concentration) from tb_history_data_content where group_name=? and element_name=?";
                    Cursor avgResult = sqLiteDatabase.rawQuery(calAvgStr, new String[]{groupName, buffer[0]});
                    if (avgResult.moveToNext() && avgResult.getFloat(0) != 0) {
                        element_average = (avgResult.getFloat(0) + historyDataContentDbValues.getAsFloat("element_concentration")) / 2;
                    }
                }

                historyDataContentDbValues.put("element_average", element_average);
                historyDataContentDbValues.put("sample_name", tempSampleName);
                sqLiteDatabase.insert("tb_history_data_content", "", historyDataContentDbValues);
            }
            br.close();


            String checkElementStr = "select element_name,element_concentration from tb_history_data_content where sample_name =?";
            Cursor checkElementCursor = sqLiteDatabase.rawQuery(checkElementStr, new String[]{tempSampleName});

            String checkMarkStr = "select mark_id,mark_num from tb_mark where mark_db_id in (select mark_db_id from tb_mark_db where mark_db_selected=?)";
            Cursor checkMarkCursor = sqLiteDatabase.rawQuery(checkMarkStr, new String[]{"true"});
            List<ElementMarkMapMode> elementAveMap = new ArrayList<ElementMarkMapMode>();
            while (checkMarkCursor.moveToNext()) {
                ElementMarkMapMode elementMarkMapItem = new ElementMarkMapMode();
                elementMarkMapItem.setMarkID(checkMarkCursor.getString(0));
                elementMarkMapItem.setMarkName(checkMarkCursor.getString(1));
                String checkMarkElementStr = "select * from tb_mark_element where mark_id=" + checkMarkCursor.getString(0);
                Cursor checkMarkElementCursor = sqLiteDatabase.rawQuery(checkMarkElementStr, null);
                while (checkMarkElementCursor.moveToNext()) {
                    ElementMarkMapElementMode elementMarkMapElementItem = new ElementMarkMapElementMode(checkMarkElementCursor.getString(checkMarkElementCursor.getColumnIndex("element_id")),
                            checkMarkElementCursor.getString(checkMarkElementCursor.getColumnIndex("element_name")),
                            checkMarkElementCursor.getString(checkMarkElementCursor.getColumnIndex("element_min_value")),
                            checkMarkElementCursor.getString(checkMarkElementCursor.getColumnIndex("element_max_value")),
                            checkMarkElementCursor.getString(checkMarkElementCursor.getColumnIndex("element_tol_value")));
                    elementMarkMapItem.elementList.add(elementMarkMapElementItem);
                }
                elementAveMap.add(elementMarkMapItem);
            }

            while (checkElementCursor.moveToNext()) {
                for (int i = 0; i < elementAveMap.size(); i++) {
                    for (ElementMarkMapElementMode elementMarkItem : elementAveMap.get(i).elementList) {
                        if (elementMarkItem.getElement_name().equals(checkElementCursor.getString(0))) {
                            if ((Float.valueOf(elementMarkItem.getElement_min_value()) - Float.valueOf(elementMarkItem.getElement_min_value()) * Float.valueOf(elementMarkItem.getElement_tol_value()) / 100.0) <= Float.valueOf(checkElementCursor.getString(1)) &&
                                    (Float.valueOf(elementMarkItem.getElement_max_value()) + Float.valueOf(elementMarkItem.getElement_max_value()) * Float.valueOf(elementMarkItem.getElement_tol_value()) / 100.0) >= Float.valueOf(checkElementCursor.getString(1))) {
                                elementAveMap.get(i).suitCount++;
                            }
                        }
                    }
                }
            }

            int maxSuitCount = 0;
            int maxSuitSite = 0;
            for (int i = 0; i < elementAveMap.size(); i++) {
                if (elementAveMap.get(i).suitCount > maxSuitCount) {
                    maxSuitCount = elementAveMap.get(i).suitCount;
                    maxSuitSite = i;
                }
            }

            ContentValues historyUpdateValues = new ContentValues();
            historyUpdateValues.put("mark_id", elementAveMap.get(maxSuitSite).getMarkID());
            historyUpdateValues.put("mark_name", elementAveMap.get(maxSuitSite).getMarkName());
            if (elementAveMap.get(maxSuitSite).suitCount == elementAveMap.get(maxSuitSite).elementList.size()) {
                historyUpdateValues.put("mark_suit_value", "SUIT_FULL");
            } else if (elementAveMap.get(maxSuitSite).suitCount <= elementAveMap.get(maxSuitSite).elementList.size() && elementAveMap.get(maxSuitSite).suitCount >= 0) {
                historyUpdateValues.put("mark_suit_value", "SUIT_PART");
            } else {
                historyUpdateValues.put("mark_suit_value", "SUIT_NULL");
            }

            sqLiteDatabase.update("tb_history_data", historyUpdateValues, "sample_name=?", new String[]{tempSampleName});

            checkElementCursor.moveToFirst();
            checkElementCursor.moveToPrevious();
            while (checkElementCursor.moveToNext()) {
                for (ElementMarkMapElementMode elementMarkItem : elementAveMap.get(maxSuitSite).elementList) {
                    if (elementMarkItem.getElement_name().equals(checkElementCursor.getString(0))) {
                        ContentValues elementRangeValues = new ContentValues();
                        elementRangeValues.put("element_range", (Integer.valueOf(elementMarkItem.getElement_min_value()) - Integer.valueOf(elementMarkItem.getElement_min_value()) * Integer.valueOf(elementMarkItem.getElement_tol_value()) / 100.0) + "~" +
                                (Integer.valueOf(elementMarkItem.getElement_max_value()) + Integer.valueOf(elementMarkItem.getElement_max_value()) * Integer.valueOf(elementMarkItem.getElement_tol_value()) / 100.0));
                        sqLiteDatabase.update("tb_history_data_content", elementRangeValues, "element_name=?", new String[]{checkElementCursor.getString(0)});
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
