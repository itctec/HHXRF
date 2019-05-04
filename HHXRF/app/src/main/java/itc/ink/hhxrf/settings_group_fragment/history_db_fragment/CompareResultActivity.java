package itc.ink.hhxrf.settings_group_fragment.history_db_fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class CompareResultActivity extends BaseActivity {
    private Switch hideSameSwitch;
    private RecyclerView compareResultRecyclerView;
    private CompareResultDataAdapter compareResultDataAdapter;
    private List<CompareResultDataMode> mSampleHistoryListData=new ArrayList<>();

    private boolean isHideSameItem=false;
    public static boolean isMultiChoiceState=false;
    public static int choiceCount=0;
    private String SampleOneName="",SampleTwoName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        Intent intent=getIntent();
        SampleOneName=intent.getStringExtra("SAMPLE_ONE_NAME");
        SampleTwoName=intent.getStringExtra("SAMPLE_TWO_NAME");
        mSampleHistoryListData=initCompareResultData(SampleOneName,SampleTwoName,mSampleHistoryListData);

        setContentView(R.layout.activity_compare_data_result);

        hideSameSwitch=findViewById(R.id.hide_Same_Item_Switch);
        hideSameSwitch.setOnCheckedChangeListener(new HideSameSwitchCheckedChangeListener());

        compareResultDataAdapter=new CompareResultDataAdapter(this, mSampleHistoryListData);
        compareResultRecyclerView=findViewById(R.id.data_Compare_Result_History_RV);
        compareResultRecyclerView.setAdapter(compareResultDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        compareResultRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMultiChoiceState=true;
        choiceCount=0;
    }

    public List<CompareResultDataMode> initCompareResultData(String sampleOneName,String sampleTwoName,List<CompareResultDataMode> compareResultItemArray) {
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        String elementSqlStr = "select element_name from tb_history_data_content where sample_name=? or sample_name=?";
        Cursor elementListCursor = sqLiteDatabase.rawQuery(elementSqlStr, new String[]{sampleOneName,sampleTwoName});

        String sampleSqlStr="select element_content from tb_history_data_content where sample_name=? and element_name=?";

        while(elementListCursor.moveToNext()){
            Cursor sampleOneResultCursor = sqLiteDatabase.rawQuery(sampleSqlStr, new String[]{sampleOneName,elementListCursor.getString(0)});
            String sampleOneResultStr="0";
            if(sampleOneResultCursor.moveToNext()){
                sampleOneResultStr=sampleOneResultCursor.getString(0);
            }
            sampleOneResultCursor.close();
            Cursor sampleTwoResultCursor = sqLiteDatabase.rawQuery(sampleSqlStr, new String[]{sampleTwoName,elementListCursor.getString(0)});
            String sampleTwoResultStr="0";
            if(sampleTwoResultCursor.moveToNext()){
                sampleTwoResultStr=sampleTwoResultCursor.getString(0);
            }
            sampleTwoResultCursor.close();
            if(isHideSameItem&&sampleOneResultStr.equals(sampleTwoResultStr)){
                continue;
            }
            CompareResultDataMode compareResultDataItem=new CompareResultDataMode(elementListCursor.getString(0),sampleOneResultStr,sampleTwoResultStr);
            compareResultItemArray.add(compareResultDataItem);
        }
        return compareResultItemArray;
    }


    public void onBackBtnClick(View view){
        Intent intent=new Intent();
        intent.setClass(CompareResultActivity.this, CompareDataActivity.class);
        startActivity(intent);
        CompareDataActivity.isMultiChoiceState=false;
        finish();
    }

    class HideSameSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            isHideSameItem=b;
            mSampleHistoryListData.clear();
            compareResultDataAdapter.notifyDataSetChanged();
            mSampleHistoryListData=initCompareResultData(SampleOneName,SampleTwoName,mSampleHistoryListData);
            compareResultDataAdapter.notifyDataSetChanged();
        }
    }


}
