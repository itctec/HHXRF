package itc.ink.hhxrf.settings_group_fragment.history_db_fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import itc.ink.hhxrf.BaseActivity;
import itc.ink.hhxrf.R;
import itc.ink.hhxrf.utils.SQLiteDBHelper;
import itc.ink.hhxrf.utils.StatusBarUtil;

public class CompareDataActivity extends BaseActivity {
    private Button compareDataBtn;
    private RecyclerView sampleHistoryRecyclerView;
    private HistoryDbDataAdapter sampleHistoryDataAdapter;
    private List<HistoryDBDataMode> mSampleHistoryListData;

    public static boolean isMultiChoiceState=false;
    public static int choiceCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        setContentView(R.layout.activity_compare_data);

        compareDataBtn=findViewById(R.id.data_Compare_Btn);
        compareDataBtn.setOnClickListener(new CompareDataBtnClickListener());

        mSampleHistoryListData=initHistoryData();
        sampleHistoryDataAdapter=new HistoryDbDataAdapter(this, mSampleHistoryListData,new ItemChoiceCallBack());
        sampleHistoryRecyclerView=findViewById(R.id.data_Compare_History_RV);
        sampleHistoryRecyclerView.setAdapter(sampleHistoryDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sampleHistoryRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMultiChoiceState=true;
        choiceCount=0;
    }

    public List<HistoryDBDataMode> initHistoryData() {
        List<HistoryDBDataMode> historyItemArray=new ArrayList<>();

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(CompareDataActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_history_data";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, null);
        while(cursor.moveToNext()){
            HistoryDBDataMode historyShowDataItem=new HistoryDBDataMode(cursor.getString(0),cursor.getString(1),cursor.getString(2),false);
            historyItemArray.add(historyShowDataItem);
        }

        return historyItemArray;
    }


    public void onBackBtnClick(View view){
        finish();
    }


    class CompareDataBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(CompareDataActivity.this,CompareResultActivity.class);
            ArrayList<String> itemCompareArray=new ArrayList<>();

            int itemCount=mSampleHistoryListData.size();
            for (int i=itemCount-1;i>=0;i--){
                if (mSampleHistoryListData.get(i).isEditSelected){
                    itemCompareArray.add(mSampleHistoryListData.get(i).getSample_name());
                }
            }
            intent.putExtra("SAMPLE_ONE_NAME",itemCompareArray.get(0));
            intent.putExtra("SAMPLE_TWO_NAME",itemCompareArray.get(1));
            startActivity(intent);
            finish();
        }
    }

    class ItemChoiceCallBack implements HistoryDbDataAdapter.ItemChoiceCallBack{
        @Override
        public void onItemChoiceEqualThanTwo() {
            compareDataBtn.setEnabled(true);
            compareDataBtn.setAlpha(1);
        }

        @Override
        public void onItemChoiceLessThanTwo() {
            compareDataBtn.setEnabled(false);
            compareDataBtn.setAlpha(0.8f);
        }
    }
}
